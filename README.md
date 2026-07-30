# Palo-Alto-Dual-ISP-Path-Monitoring-Lab
Palo Alto VM dual ISP failover lab using VyOS routers on ESXi to demonstrate the difference between route metrics and Path Monitoring.

# Palo Alto Networks Dual ISP Failover Lab Using Path Monitoring

![Palo Alto Networks](https://img.shields.io/badge/Firewall-Palo%20Alto%20VM--Series-blue)
![Virtualization](https://img.shields.io/badge/Platform-VMware%20ESXi-green)
![Routing](https://img.shields.io/badge/Technology-Dual%20ISP%20Failover-orange)
![Feature](https://img.shields.io/badge/Feature-Path%20Monitoring-red)

---

# Overview

This project demonstrates a **Dual ISP failover design using Palo Alto VM-Series Firewall with Path Monitoring**.

A common misconception in dual ISP deployments is that configuring two default routes with different metrics is enough to provide automatic Internet failover.

However, route preference alone does not verify Internet availability.

Example:

- ISP1 Default Route Metric: 10
- ISP2 Default Route Metric: 11

The firewall prefers ISP1 because it has the lower metric.

If ISP1 loses Internet connectivity but the ISP gateway remains reachable, the firewall continues using ISP1 because the next-hop device is still responding.

This results in:

- Internet outage for users.
- No automatic failover.
- Traffic continues through the failed ISP.

This lab demonstrates how **Path Monitoring** solves this issue by verifying connectivity beyond the ISP gateway.

---

# Lab Objective

The purpose of this lab is to demonstrate:

- Dual ISP connectivity design.
- Primary and backup ISP routing.
- Static route metric behavior.
- Failure detection using Path Monitoring.
- Automatic Internet failover.
- Route table changes during failure conditions.

---

# Lab Architecture

The lab is built on VMware ESXi and consists of:

- Palo Alto VM-Series Firewall.
- Two VyOS routers simulating different ISPs.
- Client workstation for traffic testing.

```
                         Internet
                            |
             +--------------+--------------+
             |                             |
        VyOS ISP1                     VyOS ISP2
      Primary ISP                    Backup ISP
             |                             |
             |                             |
             +-------------+---------------+
                           |
                  Palo Alto VM-Series
                           |
                           |
                    Internal LAN Client
```

---

# Lab Components

| Component | Description |
|-----------|-------------|
| VMware ESXi | Virtualization platform |
| Palo Alto VM-Series | Firewall and routing device |
| VyOS Router 1 | Simulates Primary ISP |
| VyOS Router 2 | Simulates Backup ISP |
| Client Machine | Generates Internet traffic |

---

# Network Addressing

## Palo Alto Interfaces

| Interface | Purpose | IP Address |
|-----------|---------|------------|
| ethernet1/1 | ISP1 Connection | 10.10.10.5/24 |
| ethernet1/2 | ISP2 Connection | 10.10.20.5/24 |
| ethernet1/3 | LAN Network | 10.2.2.1/24 |

---

## ISP Simulation

### Primary ISP (VyOS ISP1)

| Parameter | Value |
|-----------|-------|
| Device | VyOS ISP1 |
| Role | Primary Internet Provider |
| Gateway | 10.10.10.10 |

---

### Backup ISP (VyOS ISP2)

| Parameter | Value |
|-----------|-------|
| Device | VyOS ISP2 |
| Role | Backup Internet Provider |
| Gateway | 10.20.20.10 |

---

# Routing Configuration

The Palo Alto firewall contains two default routes:

| Destination | Next Hop | Metric | Purpose |
|-------------|----------|--------|---------|
| 0.0.0.0/0 | 10.10.10.10 | 10 | Primary ISP |
| 0.0.0.0/0 | 10.20.20.10 | 11 | Backup ISP |

The lower metric route is preferred.

Therefore:

```
Internet Traffic
        |
        |
     ISP1
```

---

# Path Monitoring

## Without Path Monitoring

The firewall checks only the next-hop gateway.

Example:

```
Palo Alto
    |
    |
ISP1 Gateway  ✓ Reachable
    |
    |
Internet      ✗ Down
```

The firewall believes the route is healthy and keeps forwarding traffic through ISP1.

Result:

- Users lose Internet access.
- Backup ISP is not used.

---

## With Path Monitoring Enabled

The firewall monitors a destination beyond the ISP gateway.

Example monitored addresses:

```
8.8.8.8
1.1.1.1
```

The firewall verifies:

```
Can ISP1 reach 8.8.8.8?
```

If the answer is no:

1. Palo Alto removes the primary route.
2. Backup route becomes active.
3. Traffic moves through ISP2.

Result:

```
Client
  |
Palo Alto
  |
ISP2
  |
Internet
```

---

# Testing Procedure

## Test 1 - Normal Operation

Expected:

```
Traffic uses ISP1
```

Verification:

- Check routing table.
- Verify Internet connectivity.

---

## Test 2 - ISP1 Failure Without Path Monitoring

Action:

- Simulate Internet failure behind ISP1.

Expected:

- ISP1 gateway remains reachable.
- Default route remains active.
- Traffic continues through ISP1.
- Internet access fails.

---

## Test 3 - ISP1 Failure With Path Monitoring

Action:

- Enable Path Monitoring.
- Repeat ISP1 failure.

Expected:

- Palo Alto detects unreachable monitored destination.
- Primary route is removed.
- Backup ISP becomes active.
- Internet connectivity is restored.

---

# Lab Credentials

## Palo Alto VM-Series Firewall

| Parameter | Value |
|-----------|-------|
| Username | admin |
| Password | O@123456@o |

---

## VyOS Routers

### ISP1 Router

| Parameter | Value |
|-----------|-------|
| Username | vyos |
| Password | vyos |

---

### ISP2 Router

| Parameter | Value |
|-----------|-------|
| Username | vyos |
| Password | vyos |

---

# VMware ESXi Design

Recommended virtual switches:

```
vSwitch-ISP1
     |
 VyOS ISP1
     |
 Palo Alto ethernet1/1


vSwitch-ISP2
     |
 VyOS ISP2
     |
 Palo Alto ethernet1/2


vSwitch-LAN
     |
 Palo Alto ethernet1/3
     |
 Client VM
```

---

# Repository Structure

```
PaloAlto-Dual-ISP-Path-Monitoring-Lab

│
├── README.md
│
├── topology
│   ├── Lab-Topology.png
│   └── Lab-Topology.drawio
│
├── configs
│   ├── PaloAlto
│   └── VyOS
│
├── screenshots
│   ├── topology.png
│   ├── routing-table.png
│   ├── path-monitoring.png
│   └── failover-test.png
│
└── documentation
    └── Lab-Guide.pdf
```

---

# Screenshots

The following screenshots demonstrate the lab:

- Lab topology.
- Palo Alto interfaces.
- Static route configuration.
- Path Monitoring configuration.
- Routing table before failure.
- Routing table after failover.
- Successful traffic recovery.

---

# Key Learning Points

This lab demonstrates:

- Route metrics select preferred paths.
- Gateway reachability does not guarantee Internet availability.
- Path Monitoring provides true path validation.
- Palo Alto can automatically remove failed routes.
- Dual ISP designs require health monitoring for reliable failover.

---

# Conclusion

A dual ISP design should not rely only on route metrics.

Metrics determine the preferred route, but Path Monitoring verifies whether the path is actually working.

By combining static route preference with Path Monitoring, Palo Alto firewalls can provide reliable Internet failover and improve network availability.

---

 Author

**Mahmoud Obaid**

Telecommunication Technology Engineer  
Senior IT Analyst

This project was created as a practical network security lab demonstrating enterprise-style Internet redundancy and automatic failover using:

- Palo Alto Networks VM-Series Firewall
- VyOS Virtual Routers
- VMware ESXi Hypervisor

Areas Covered:
- Dual ISP Architecture
- Static Route Preference
- Path Monitoring
- Automatic ISP Failover
- Network Availability Design
- Firewall and Routing Validation

GitHub:
https://github.com/Mahmoud-Obaid
