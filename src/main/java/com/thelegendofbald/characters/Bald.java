package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.thelegendofbald.view.main.TileMap;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.model.weapons.Weapon;
import com.thelegendofbald.view.main.TileMap;


public class Bald extends Entity implements Combatant {

    private static final int WIDTH = 50; // Larghezza del frame
    private static final int HEIGHT = 50; // Altezza del frame

    private Optional<Weapon> weapon = Optional.empty();
    public static final String SpeedX = null;
    private TileMap tileMap;
    private int attackPower; // Potenza d'attacco
    private BufferedImage image;
    private String path = "/images/bald.png"; // Percorso dell'immagine
    private double speedX = 0.0; // Velocità lungo l'asse X
    private final Wallet wallet = new Wallet(0);

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public Wallet getWallet() {
        return wallet;
    }

    private double speedY = 0.0; // Velocità lungo l'asse Y
    private double posX, posY;
    private BufferedImage[] runFrames; // Array di immagini per l'animazione della corsa
    private Map<String, BufferedImage[]> attackFrames = new HashMap<>(); // Mappa per i frame di attacco
    private BufferedImage[] actualAttackFrames;
    private int currentFrame = 0; // Indice del frame corrente
    private int frameDelay = 5; // Numero di aggiornamenti prima di cambiare frame
    private int frameCounter = 0; // Contatore per il ritardo tra i frame
    private int MOVE_SPEED = 60; // Velocità di movimento in pixel al secondo
    private boolean isAttacking = false; // Indica se Bald sta attaccando
    private int currentAttackFrame = 0; // Indice del frame corrente nell'animazione di attacco
    private boolean facingRight = true; // Direzione in cui Bald sta guardando
    private int health = 100; // Salute di Bald
    private String name; // Nome di Bald

    public Bald(int x, int y, int maxHealth, String name, int attackPower) {
        super(x, y, WIDTH, HEIGHT, name, new LifeComponent(maxHealth));
        this.attackPower = attackPower;
        this.posX = x;
        this.posY = y;
        loadRunFrames();
        loadAllAttackFrames();
    }

    public void startAttackAnimation() {
        weapon.ifPresent(w -> actualAttackFrames = attackFrames.getOrDefault(w.getName().toLowerCase(),
                attackFrames.get("def")));
        currentAttackFrame = 0;
    }

    private void loadAllAttackFrames() {
        final String defaultPath = "/images/bald_attack";
        List<String> weaponNames = List.of("def", "sword", "axe");
        int numFrames = 8;

        weaponNames.forEach(weaponName -> {
            String startingPath = defaultPath + "/" + weaponName;
            BufferedImage[] frames = new BufferedImage[numFrames];

            Stream.iterate(0, i -> i + 1).limit(numFrames).forEach(i -> {
                String framePath = startingPath + String.format("/frame_%d.png", i);
                InputStream inputStream = this.getClass().getResourceAsStream(framePath);

                Optional.ofNullable(inputStream).ifPresentOrElse(is -> {
                    try {
                        frames[i] = ImageIO.read(is);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }, () -> System.err.println("Frame " + framePath + " not found"));
            });

            attackFrames.put(weaponName, frames);
        });

        System.out.println("Attack frames loaded: " + attackFrames.size());
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }    

    public void setSpawnPosition(int spawnTileId, int tileSize) {
        Point spawnPoint = tileMap.findSpawnPoint(spawnTileId);
        if (spawnPoint != null) {
            // Centra i piedi di Bald nel tile di spawn
            int x = spawnPoint.x + (tileSize - getWidth()) / 2;
            int y = spawnPoint.y + tileSize - getHeight();
            this.setX(x);
            this.setY(y);
        }
    }

    private void loadRunFrames() {
        try {
            int numFrames = 9; // Supponiamo di avere 6 frame
            runFrames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                String framePath = String.format("/images/bald_run/PS_BALD GUY_Run_00%d.png", i + 1); // Percorso dei
                                                                                                      // frame
                InputStream is = getClass().getResourceAsStream(framePath);
                if (is != null) {
                    runFrames[i] = ImageIO.read(is);
                } else {
                    System.err.println("Frame " + framePath + " not found");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;

            if (isAttacking && actualAttackFrames != null) {
                currentAttackFrame++;
                if (currentAttackFrame >= actualAttackFrames.length) {
                    isAttacking = false;
                    currentAttackFrame = 0;
                }

            } else {
                currentFrame = (currentFrame + 1) % runFrames.length; // Cicla tra i frame
            }
        }

    }

    public void render(Graphics g) {
        if (isAttacking && actualAttackFrames != null && actualAttackFrames[currentAttackFrame] != null) {
            if (!facingRight) {
                // Disegna normalmente se Bald è girato verso destra
                g.drawImage(actualAttackFrames[currentAttackFrame], x, y, 50, 50, null);
            } else {
                // Disegna riflettendo l'immagine orizzontalmente
                g.drawImage(actualAttackFrames[currentAttackFrame], x + 50, y, -50, 50, null);
            }
        } else if (runFrames != null && runFrames[currentFrame] != null) {
            if (!facingRight) {
                // Disegna normalmente se Bald è girato verso destra
                g.drawImage(runFrames[currentFrame], x, y, getWidth(), getHeight(), null);
            } else {
                // Disegna riflettendo l'immagine orizzontalmente
                g.drawImage(runFrames[currentFrame], x + 50, y, -50, getHeight(), null);
            }
        } else {
            // Fallback al quadrato rosso se i frame non sono caricati
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
        // this.updateAnimation();
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
        // this.updateAnimation();
    }

    public void move(TileMap tileMap, double deltaTime) {

        int hitboxX = 15;
        int hitboxY = 25;

        double nextX = posX + speedX * deltaTime * MOVE_SPEED; // deltaTime per rendere il movimento costante al variare degli FPS
        Rectangle nextHitboxX = new Rectangle(
            (int)(nextX + (50 - hitboxX) / 2),
            (int)(posY + (50 - hitboxY) / 2),
            hitboxX, hitboxY
        );

        int tileSize = 32;
        int leftX = nextHitboxX.x / tileSize;
        int rightX = (nextHitboxX.x + nextHitboxX.width - 1) / tileSize;
        int topX = nextHitboxX.y / tileSize;
        int bottomX = (nextHitboxX.y + nextHitboxX.height - 1) / tileSize;

        boolean collisionX = false;
        for (int tx = leftX; tx <= rightX; tx++) {
            for (int ty = topX; ty <= bottomX; ty++) {
                if (tileMap.getTileIdAt(tx, ty) == 2) {
                    collisionX = true;
                    break;
                }
            }
            if (collisionX) break;
        }

        if (!collisionX) {
            posX = nextX;
        }

        double nextY = posY + speedY * deltaTime * MOVE_SPEED;
        Rectangle nextHitboxY = new Rectangle(
            (int)(posX + (50 - hitboxX) / 2),
            (int)(nextY + (50 - hitboxY) / 2),
            hitboxX, hitboxY
        );

        int leftY = nextHitboxY.x / tileSize;
        int rightY = (nextHitboxY.x + nextHitboxY.width - 1) / tileSize;
        int topY = nextHitboxY.y / tileSize;
        int bottomY = (nextHitboxY.y + nextHitboxY.height - 1) / tileSize;

        boolean collisionY = false;
        for (int tx = leftY; tx <= rightY; tx++) {
            for (int ty = topY; ty <= bottomY; ty++) {
                if (tileMap.getTileIdAt(tx, ty) == 2) {
                    collisionY = true;
                    break;
                }
            }
            if (collisionY) break;
        }

        if (!collisionY) {
            posY = nextY;
        }

        x = (int) posX;
        y = (int) posY;
    }

    public Rectangle getHitbox() {
        int width = 15;
        int height = 25;
        int offsetX = x + (50 - width) / 2;
        int offsetY = y + (50 - height) / 2;
        return new Rectangle(offsetX, offsetY, width, height);
    }


    public int getHeight() {
        return 50;
    }

    public int getWidth() {
        return 50;
    }

    @Override
    public void takeDamage(int damage) {
        this.lifeComponent.damageTaken(damage);
        System.out.println(lifeComponent.getCurrentHealth());

    }

    public boolean isAlive() {
        return !this.lifeComponent.isDead();
    }

    public boolean canShoot() {
        return true;
    }

    public void shootProjectile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shootProjectile'");
    }

    public Optional<Weapon> getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = Optional.ofNullable(weapon);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void attack() {
        this.isAttacking = true;
        this.startAttackAnimation();
    }

}
