package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.client.particle.BasicColorParticleConstructor;
import com.uberhelixx.flatlights.client.particle.BasicColorParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class ParticleHelper {
    public static ParticleOptions constructSimpleSpark(Color color, float diameter, int lifetime, float scaleModifier) {
        return new BasicColorParticleOptions(BasicColorParticleConstructor.builder()
                .color(color.getRGB())
                .diameter(diameter)
                .lifetime(lifetime)
                .scaleModifier(scaleModifier)
                .roll(0.5F)
                .build());
    }
    
    /**
     * Creates a ball using the input particle
     * @param particle The particle used to form the ball
     * @param vec The location where the ball should be formed
     * @param level The level
     * @param density Density of the particles used in the ball
     * @param speed Speed at which the particles move
     */
    public static void createBall(ParticleOptions particle, Vec3 vec, Level level, int density, float speed) {
        if (!level.isClientSide()) {
            return;
        }
        
        for (int i = -density; i <= density; ++i) {
            for (int j = -density; j <= density; ++j) {
                for (int k = -density; k <= density; ++k) {
                    double d3 = (double) j + (level.random.nextDouble() - level.random.nextDouble()) * 0.5D;
                    double d4 = (double) i + (level.random.nextDouble() - level.random.nextDouble()) * 0.5D;
                    double d5 = (double) k + (level.random.nextDouble() - level.random.nextDouble()) * 0.5D;
                    double d6 = (double) Mth.sqrt((float) (d3 * d3 + d4 * d4 + d5 * d5)) / speed + level.random.nextGaussian() * 0.05D;
                    
                    level.addParticle(particle, vec.x(), vec.y(), vec.z(), d3 / d6, d4 / d6, d5 / d6);
                    
                    if (i != -density && i != density && j != -density && j != density) {
                        k += density * 2 - 1;
                    }
                }
            }
        }
    }
    
    /**
     * Create a cylinder of particles
     * @param particle The particle
     * @param center Center of where the cylinder should be created
     * @param level The world
     * @param radius Radius of the cylinder
     * @param step How small the distance should be between particles that make up a ring of the cylinder
     */
    public static void createCyl(ParticleOptions particle, Vec3 center, Level level, double radius, float step) {
        int offset = 16;
        double len = (float) (2 * Math.PI * radius);
        int num = (int) (len / step);
        
        for (int i = 0; i < num; i++) {
            double angle = Math.toRadians(((360F / num) * i) + (360F * ((((len / step) - num) / num) / len)));
            
            double extraX = (radius * Math.sin(angle)) + center.x();
            double extraZ = (radius * Math.cos(angle)) + center.z();
            double extraY = center.y() + 0.5F;
            
            boolean foundPos = false;
            
            int tries;
            
            for (tries = 0; tries < offset * 2; tries++) {
                Vec3 vec = new Vec3(extraX, extraY, extraZ);
                BlockPos pos = new BlockPos((int) vec.x, (int) vec.y, (int) vec.z);
                BlockState state = level.getBlockState(pos);
                VoxelShape shape = state.getCollisionShape(level, pos);
                
                if (state.getBlock() instanceof LiquidBlock liquid) {
                    AABB aabb = new AABB(pos);
                    aabb.inflate(-0.5);
                    shape = Shapes.block();
                }
                if (shape.isEmpty()) {
                    if (!foundPos) {
                        extraY -= 1;
                        continue;
                    }
                }
                else {
                    foundPos = true;
                }
                if (shape.isEmpty()) {
                    break;
                }
                AABB aabb = shape.bounds();
                if (!aabb.move(pos).contains(vec)) {
                    if (aabb.maxY >= 1F) {
                        extraY += 1;
                        continue;
                    }
                    break;
                }
                extraY += step;
            }
            if (tries < offset * 2) {
                level.addParticle(particle, extraX, extraY + 0.1F, extraZ, 0, 0, 0);
            }
        }
    }
    
    /**
     * Creates a cylinder of particles which float in the specified direction
     * @param particle The particle used to create the cylinder
     * @param level The world where the particles are created
     * @param rings Number of rings generated for the cylinder
     * @param motion Direction that the particles will move
     * @param length Amount of space between each ring in the cylinder
     * @param center Center of where the cylinder should be created
     * @param radius Radius of the cylinder
     * @param step How small of a distance between particles used to form a circle of the cylinder
     */
    public static void createCylinder(ParticleOptions particle, Level level, int rings, Vec3 motion, double length, Vec3 center, double radius, float step) {
        double len = (float) (2 * Math.PI * radius);
        int num = (int) (len / step);
        
        for (int i = 0; i < num; i++) {
            double angle = Math.toRadians(((360F / num) * i) + (360F * ((((len / step) - num) / num) / len)));
            
            double extraX = (radius * Math.sin(angle)) + center.x();
            double extraZ = (radius * Math.cos(angle)) + center.z();
            double extraY = center.y() + 0.5F;
            
            Vec3 start = new Vec3(extraX, extraY, extraZ);
            Vec3 end = new Vec3(extraX + motion.x() * length, extraY + motion.y() * length, extraZ + motion.z() * length);
            
            createLine(particle, level, start, end, rings, motion);
        }
    }
    
    public static void createLine(ParticleOptions particle, Level level, Vec3 start, Vec3 end, int amount, Vec3 motion) {
        Vec3 delta = end.subtract(start);
        Vec3 dir = delta.normalize();
        
        for (int i = 0; i < amount; ++i) {
            double progress = i * delta.length() / amount;
            
            level.addParticle(particle, start.x + dir.x * progress, start.y + dir.y * progress,
                    start.z + dir.z * progress, motion.x, motion.y, motion.z);
        }
    }
    
    public static void createLine(ParticleOptions particle, Level level, Vec3 start, Vec3 end, int amount) {
        createLine(particle, level, start, end, amount, Vec3.ZERO);
    }
}
