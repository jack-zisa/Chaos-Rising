using UnityEngine;
using System;
using System.Collections.Generic;

[RequireComponent(typeof(StatContainer))]
public class Living : MonoBehaviour
{
    public int health, magic;

    public event EventHandler<DamageEventArgs> onDamaged;

    [HideInInspector] public ChaosRising.Stats stats;
    [HideInInspector] public Dictionary<int, Action> statBars = new Dictionary<int, Action>();

    private void Awake()
    {
        stats = GetComponent<StatContainer>().stats;

        health = stats.maxHealth;
        magic = stats.maxMagic;
    }

    private void Start()
    {
        onDamaged += Living_OnDamage;
    }

    private void OnEnable()
    {
        if (stats != null)
        {
            health = stats.maxHealth;
            magic = stats.maxMagic;
        }
    }

    private void OnParticleCollision(GameObject other)
    {
        ParticleSystem system = other.GetComponent<ParticleSystem>();

        ParticleSystem.MinMaxCurve damageCurve = system.customData.GetVector(ParticleSystemCustomData.Custom1, 0);
        int damage = UnityEngine.Random.Range((int)damageCurve.constantMin, (int)damageCurve.constantMax);
        int armorIgnored = (int)system.customData.GetVector(ParticleSystemCustomData.Custom1, 1).constantMin;

        if (onDamaged != null) onDamaged?.Invoke(this, new DamageEventArgs { damage = damage, armorIgnored = armorIgnored });
    }

    public void Living_OnDamage(object sender, DamageEventArgs e)
    {
        if (e.armorIgnored >= stats.armor) e.damage += stats.armor;
        else e.damage += e.armorIgnored;

        if (health - e.damage <= 0)
        {
            Kill();
        }
        else health -= e.damage;

        if (statBars.ContainsKey(0))
        {
            statBars[0].Invoke();
        }
    }

    public class DamageEventArgs : EventArgs
    {
        public int damage;
        public int armorIgnored;
    }

    private void Kill()
    {
        health = 0;
        gameObject.SetActive(false);
    }
}
