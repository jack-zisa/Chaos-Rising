using ChaosRising;
using UnityEngine;
using System.Collections.Generic;

public class ProjectileGenerator : MonoBehaviour
{
    public Attack[] attacks;
    public List<KeyValue<ParticleSystem>> emitters;

    private Stats stats;
    private PlayerController targetPlayer;

    private Vector3 directionToPlayer;
    private Vector3 mousePosition;

    private Color empty = new Color(0f, 0f, 0f, 0f);
    private Color full = new Color(1f, 1f, 1f, 1f);

    private void Awake()
    {
        stats = GetComponentInParent<StatContainer>().stats;
    }

    [System.Obsolete]
    private void Start()
    {
        emitters = new List<KeyValue<ParticleSystem>>();
        mousePosition = MouseUtility.GetMousePosition();

        for (int i = 0; i < attacks.Length; ++i)
        {
            attacks[i].startAngle += 90;
            for (int j = 0; j < attacks[i].projectileCount; ++j)
            {
                GameObject obj = new GameObject(name + i + j);
                obj.transform.parent = transform;
                CreateGenerator(i, attacks[i], obj.AddComponent<ParticleSystem>());
            }
        }
    }

    [System.Obsolete]
    private void CreateGenerator(int attackIndex, Attack attack, ParticleSystem particleSystem)
    {
        emitters.Add(new KeyValue<ParticleSystem>(attackIndex, particleSystem));

        particleSystem.transform.position = attack.spawnPositionType == PositionType.Origin ? transform.parent.position : mousePosition;
        particleSystem.transform.position += new Vector3(attack.offset.x  / 10f, attack.offset.y / 10f, 0f);
        particleSystem.transform.rotation = Quaternion.Euler(-90f, 0f, 0f);

        particleSystem.loop = false;
        particleSystem.startSize = .1f;
        particleSystem.startSpeed = attack.speed;
        particleSystem.startLifetime = attack.lifetime;

        #region Tint
        UnityEngine.Color tint = new Color(attack.display.tintRed, attack.display.tintGreen, attack.display.tintBlue, attack.display.tintAlpha);
        if (tint != empty)
        {
            particleSystem.startColor = tint;
        }
        else
        {
            particleSystem.startColor = UnityEngine.Color.white;
        }
        #endregion

        #region Damage
        ParticleSystem.CustomDataModule data = particleSystem.customData;
        data.enabled = true;
        data.SetMode(ParticleSystemCustomData.Custom1, ParticleSystemCustomDataMode.Vector);
        data.SetVectorComponentCount(ParticleSystemCustomData.Custom1, 2);
        data.SetVector(ParticleSystemCustomData.Custom1, 0, new ParticleSystem.MinMaxCurve(attack.minDamage, attack.maxDamage));
        data.SetVector(ParticleSystemCustomData.Custom1, 1, new ParticleSystem.MinMaxCurve(attack.armorIgnored, 0f));
        #endregion

        #region Main
        ParticleSystem.MainModule main = particleSystem.main;
        main.emitterVelocityMode = ParticleSystemEmitterVelocityMode.Transform;
        main.simulationSpace = ParticleSystemSimulationSpace.World;
        #endregion

        #region Inherit
        ParticleSystem.InheritVelocityModule inheritVelocity = particleSystem.inheritVelocity;
        inheritVelocity.curveMultiplier = attack.inheritedVelocity;
        #endregion

        #region Emission
        ParticleSystem.EmissionModule emission = particleSystem.emission;
        emission.rate = 0f;
        ParticleSystem.Burst[] bursts = new ParticleSystem.Burst[attack.bursts.Length];
        for (int i = 0; i < bursts.Length; ++i)
        {
            bursts[i] = new ParticleSystem.Burst();
            bursts[i].time = 0f;
            bursts[i].count = attack.bursts[i].count;
            bursts[i].cycleCount = attack.bursts[i].cycles;
            bursts[i].repeatInterval = attack.bursts[i].interval;
        }
        emission.SetBursts(bursts);
        #endregion

        #region Shape
        ParticleSystem.ShapeModule shape = particleSystem.shape;
        shape.shapeType = ParticleSystemShapeType.Cone;
        shape.angle = 0f;
        shape.radius = 0f;
        
        if (attack.pingPong.width > 0f)
        {
            shape.arcMode = ParticleSystemShapeMultiModeValue.PingPong;
            shape.radius = attack.pingPong.width;
            shape.arcSpeed = attack.pingPong.speed;
            shape.arcSpread = attack.pingPong.spread;
        }
        #endregion

        #region Texture Sheet Animation
        ParticleSystem.TextureSheetAnimationModule textureSheetAnimation = particleSystem.textureSheetAnimation;
        textureSheetAnimation.enabled = true;
        textureSheetAnimation.mode = ParticleSystemAnimationMode.Sprites;
        textureSheetAnimation.AddSprite(AssetManager.GetProjectileSprite(attack.projectile));
        #endregion

        #region Collision
        ParticleSystem.CollisionModule collision = particleSystem.collision;
        collision.enabled = true;
        collision.mode = ParticleSystemCollisionMode.Collision2D;
        collision.type = ParticleSystemCollisionType.World;
        collision.quality = ParticleSystemCollisionQuality.High;
        collision.dampen = attack.collision.dampen;
        collision.bounce = attack.collision.bounce;
        collision.lifetimeLoss = attack.collision.lifetimeLoss;
        collision.sendCollisionMessages = true;
        collision.collidesWith = LayerMask.GetMask("Blocking", "Obstacle", transform.parent.tag.Equals("Player") ? "Enemy" : "Player");
        #endregion

        #region GPU & Material
        ParticleSystemRenderer renderer = particleSystem.GetComponent<ParticleSystemRenderer>();
        renderer.sharedMaterial = Resources.Load<Material>("Materials/Sprites");
        renderer.enableGPUInstancing = true;
        renderer.sortingLayerName = "Projectile";
        #endregion

        #region Size Over Time
        if (attack.display.sizeChange > 0f)
        {
            ParticleSystem.SizeOverLifetimeModule sizeOverLifetime = particleSystem.sizeOverLifetime;
            sizeOverLifetime.enabled = true;
            Keyframe[] sizes = new Keyframe[2];
            sizes[0] = new Keyframe(0f, 0f);
            sizes[0] = new Keyframe(attack.display.sizeOffset, attack.display.sizeChange);
            sizeOverLifetime.size = new ParticleSystem.MinMaxCurve(1f, new AnimationCurve(sizes));
        }
        #endregion

        #region Rotate Over Time
        if (attack.display.rotateChange > 0f)
        {
            ParticleSystem.RotationOverLifetimeModule rotationOverLifetime = particleSystem.rotationOverLifetime;
            rotationOverLifetime.enabled = true;
            Keyframe[] rotations = new Keyframe[2];
            rotations[0] = new Keyframe(0f, 0f);
            rotations[0] = new Keyframe(attack.display.rotateOffset, attack.display.rotateChange);
            rotationOverLifetime.z = new ParticleSystem.MinMaxCurve(1f, new AnimationCurve(rotations));
        }
        #endregion

        #region Color Over Time
        if (attack.display.alphaChange > 0f)
        {
            ParticleSystem.ColorOverLifetimeModule colorOverLifetime = particleSystem.colorOverLifetime;
            colorOverLifetime.enabled = true;

            GradientColorKey[] colors = new GradientColorKey[2];
            colors[0] = new GradientColorKey(full, 0f);
            colors[1] = new GradientColorKey(new Color(attack.display.colorChangeRed, attack.display.colorChangeGreen, attack.display.colorChangeBlue, attack.display.colorChangeAlpha), attack.display.colorOffset);

            GradientAlphaKey[] alphas = new GradientAlphaKey[2];
            alphas[0] = new GradientAlphaKey(0f, 0f);
            alphas[1] = new GradientAlphaKey(attack.display.alphaChange, attack.display.alphaOffset);

            Gradient gradient = new Gradient();
            if (!attack.display.blend) gradient.mode = GradientMode.Fixed;
            gradient.SetKeys(colors, alphas);
            colorOverLifetime.color = new ParticleSystem.MinMaxGradient(gradient);
        }
        #endregion

        #region Acceleration

        ParticleSystem.VelocityOverLifetimeModule velocityOverLifetime = particleSystem.velocityOverLifetime;
        velocityOverLifetime.enabled = true;
        velocityOverLifetime.space = ParticleSystemSimulationSpace.World;
        
        if (attack.acceleration.acceleration > 0f)
        {
            Keyframe[] accKeyFrame = new Keyframe[2];
            accKeyFrame[0] = new Keyframe(0f, 0f);
            accKeyFrame[1] = new Keyframe(attack.acceleration.offset, attack.acceleration.acceleration);
            velocityOverLifetime.speedModifier = new ParticleSystem.MinMaxCurve(1f, new AnimationCurve(accKeyFrame));
        }


        if (attack.acceleration.max >= 0f)
        {
            ParticleSystem.LimitVelocityOverLifetimeModule limitVelocityOverLifetime = particleSystem.limitVelocityOverLifetime;
            limitVelocityOverLifetime.enabled = true;
            limitVelocityOverLifetime.space = ParticleSystemSimulationSpace.World;

            limitVelocityOverLifetime.limit = attack.acceleration.max;
        }
        #endregion

        #region Orbit
        velocityOverLifetime.orbitalYMultiplier = attack.orbit.speed;
        if (attack.orbit.radial) velocityOverLifetime.radial = 1;
        #endregion

        #region Death Emission
        //if (attack.deathEmission.attack != null && !sub && false)
        //{
        //    GameObject obj = new GameObject(name + "_subEmitter");
        //    obj.transform.parent = transform.GetChild(index);

        //    ParticleSystem subSystem = obj.AddComponent<ParticleSystem>();
        //    CreateGenerator(-1, attack.deathEmission.attack, subSystem, true);

        //    ParticleSystem.SubEmittersModule subEmitters = particleSystem.subEmitters;
        //    subEmitters.enabled = true;
        //    subEmitters.AddSubEmitter(subSystem, ParticleSystemSubEmitterType.Death, ParticleSystemSubEmitterProperties.InheritNothing, attack.deathEmission.chance);
        //}
        #endregion
    }

    public void SetTargetPlayer(PlayerController targetPlayer)
    {
        this.targetPlayer = targetPlayer;
    }

    public void UpdateAttack(int attackIndex)
    {
        Attack attack = attacks[attackIndex];
        if ((attack.attackTime -= Time.deltaTime) < 0f)
        {
            ParticleSystem.EmitParams emission = new ParticleSystem.EmitParams();
            ParticleSystem.ShapeModule shape;

            float angle = 0f;
            if (attack.targetType == TargetType.Fixed)
            {
                angle = attack.startAngle += attack.angleChange;
            }
            else if (attack.targetType == TargetType.Player)
            {
                if (targetPlayer != null) directionToPlayer = targetPlayer.transform.position - gameObject.transform.position;
                else directionToPlayer = Vector3.zero;

                if (attack.targetPrediction != 0f)
                {
                    angle = MathUtility.ToAngle((directionToPlayer + targetPlayer.movement.normalized) * attack.targetPrediction);
                }
                else
                {
                    angle = MathUtility.ToAngle(directionToPlayer);
                }
            }
            else if (attack.targetType == TargetType.Mouse)
            {
                mousePosition = MouseUtility.GetMousePosition();
                angle = MouseUtility.GetMouseAngle(transform.position, false);
            }

            if (attack.projectileCount > 1) angle -= attack.angleGap * (((attack.projectileCount - 1) / 2f) + 1);
            if (attack.startAngle >= 360) attack.startAngle -= 360;

            ParticleSystem particleSystem;
            for (int j = 0; j < emitters.Count; ++j)
            {
                if (emitters[j].key == attackIndex)
                {
                    particleSystem = emitters[j].value;
                    if (attack.spawnPositionType == PositionType.Mouse)
                    {
                        particleSystem.transform.position = MouseUtility.GetMousePosition();
                    }

                    shape = particleSystem.shape;
                    shape.rotation = new Vector3(0f, angle += attack.angleGap, 0f);

                    if (attack.display.maxSize > 0)
                    {
                        emission.startSize = Random.Range(attack.display.minSize, attack.display.maxSize);
                    }
                    emission.rotation = attack.startRotation + angle;

                    particleSystem.Emit(emission, 1);
                    attack.attackTime = 1f / (stats.dexterity * attack.rateOfFire);
                }
            }
        }
    }

    public void UpdateAttacks()
    {
        for (int i = 0; i < attacks.Length; ++i)
        {
            UpdateAttack(i);
        }
    }
}
