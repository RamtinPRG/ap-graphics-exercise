package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Tree {

    private Animation<TextureRegion> anim;
    private float x, y;
    private float stateTime = MathUtils.random(0.8f);
    private float cooldownTime = 0f;
    private float pauseTime = 0f;

    private final float delayAfterAnimation = 10f;
    private final float pauseDuration = 2f; // Pause on frame 1 for 2 seconds
    private final int pauseFrameIndex = 2;

    private enum State {
        PLAYING, PAUSED_ON_FRAME, COOLDOWN
    }
    private State currentState = State.PLAYING;

    public Tree(float x, float y) {
        this.x = x;
        this.y = y;
        anim = loadAnimation("Enemies/Tree/", 0.2f);
        anim.setPlayMode(Animation.PlayMode.NORMAL);
        // int initialState = MathUtils.random(2);
        // switch (initialState) {
        //     case 0:
        //         currentState = State.PLAYING;
        //         break;
        //     case 1:
        //         currentState = State.PAUSED_ON_FRAME;
        //         anim.
        //         break;
        //     case 2:
        //         currentState = State.COOLDOWN;
        //         break;
        // }
    }

    public void render(SpriteBatch batch, float delta) {
        switch (currentState) {
            case PLAYING:
                stateTime += delta;

                // Check if we hit the pause frame
                int currentFrameIndex = anim.getKeyFrameIndex(stateTime);
                if (currentFrameIndex == pauseFrameIndex) {
                    currentState = State.PAUSED_ON_FRAME;
                    pauseTime = 0f;
                    break;
                }

                // Finished animation?
                if (anim.isAnimationFinished(stateTime)) {
                    currentState = State.COOLDOWN;
                    cooldownTime = 0f;
                }
                break;

            case PAUSED_ON_FRAME:
                pauseTime += delta;
                if (pauseTime >= pauseDuration) {
                    currentState = State.PLAYING;
                    float frameDuration = anim.getFrameDuration();
                    stateTime += frameDuration / 2f;
                }
                break;

            case COOLDOWN:
                cooldownTime += delta;
                if (cooldownTime >= delayAfterAnimation) {
                    currentState = State.PLAYING;
                    stateTime = 0f;
                }
                break;
        }

        // Determine which frame to draw
        TextureRegion frame;
        if (currentState == State.PAUSED_ON_FRAME) {
            frame = anim.getKeyFrames()[pauseFrameIndex];
        } else {
            frame = anim.getKeyFrame(stateTime, false);
        }
        batch.draw(frame, x, y);
    }

    private Animation<TextureRegion> loadAnimation(String folder, float frameDuration) {
        FileHandle dir = Gdx.files.internal(folder);
        FileHandle[] files = dir.list();
        // Arrays.sort(files, Comparator.comparing(FileHandle::name)); // Ensure consistent order

        System.out.println("Loading animation from: " + folder + files.length + dir.isDirectory());
        for (FileHandle file : files) {
            System.out.println("Found file: " + file.name());
        }

        TextureRegion[] frames = new TextureRegion[files.length];
        for (int i = 0; i < files.length; i++) {
            frames[i] = new TextureRegion(new Texture(files[i]));
        }
        return new Animation<>(frameDuration, frames);
    }
}
