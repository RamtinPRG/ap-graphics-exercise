package com.ramtinprg.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ramtinprg.model.enemies.Enemy;
import com.ramtinprg.model.enemies.EyeBat;
import com.ramtinprg.model.enemies.TentacleMonster;

public class EnemySpawner {

    private float timeElapsed = 0;
    private float tentacleSpawnTimer = 0;
    private float eyebatSpawnTimer = 0;
    private final float gameDuration;

    public EnemySpawner(float gameDuration) {
        this.gameDuration = gameDuration;
    }

    public void update(float delta, Array<Enemy> enemies, Camera camera) {
        timeElapsed += delta;
        tentacleSpawnTimer += delta;
        eyebatSpawnTimer += delta;

        // Tentacle spawn every 3s
        if (tentacleSpawnTimer >= 3f) {
            tentacleSpawnTimer = 0;
            int count = (int) (timeElapsed / 30f);
            for (int i = 0; i < count; i++) {
                spawnTentacle(enemies, camera);
            }
        }

        // Eyebat spawns start after t/4 seconds
        if (timeElapsed > gameDuration / 4f && eyebatSpawnTimer >= 10f) {
            eyebatSpawnTimer = 0;
            float i = timeElapsed;
            int count = (int) ((4f * i - gameDuration + 30f) / 30f);
            for (int j = 0; j < count; j++) {
                spawnEyebat(enemies, camera);
            }
        }
    }

    private void spawnTentacle(Array<Enemy> enemies, Camera camera) {
        // System.out.println("Tentacle spawned");
        Vector2 pos = getOffScreenSpawn(camera, 100);
        float x = pos.x;
        float y = pos.y;
        enemies.add(new TentacleMonster(x, y));
    }

    private void spawnEyebat(Array<Enemy> enemies, Camera camera) {
        // System.out.println("Eyebat spawned");
        Vector2 pos = getOffScreenSpawn(camera, 100);
        float x = pos.x;
        float y = pos.y;
        enemies.add(new EyeBat(x, y));
    }

    public static Vector2 getOffScreenSpawn(Camera camera, float padding) {
        float camLeft = camera.position.x - camera.viewportWidth / 2;
        float camRight = camera.position.x + camera.viewportWidth / 2;
        float camBottom = camera.position.y - camera.viewportHeight / 2;
        float camTop = camera.position.y + camera.viewportHeight / 2;

        int side = MathUtils.random(3); // 0=left, 1=right, 2=top, 3=bottom

        float x = 0, y = 0;

        switch (side) {
            case 0: // Left
                x = camLeft - padding;
                y = MathUtils.random(camBottom, camTop);
                break;
            case 1: // Right
                x = camRight + padding;
                y = MathUtils.random(camBottom, camTop);
                break;
            case 2: // Top
                x = MathUtils.random(camLeft, camRight);
                y = camTop + padding;
                break;
            case 3: // Bottom
                x = MathUtils.random(camLeft, camRight);
                y = camBottom - padding;
                break;
        }

        return new Vector2(x, y);
    }
}
