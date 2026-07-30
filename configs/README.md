# Network Configuration Files

This folder contains configuration files for the network devices used in this lab/project.

## Files

|          File        |         Description              |
|----------------------|----------------------------------|
| `running-config.xml` | Configuration for Palo Alto FW.  |
|   `ISP2config.boot`  | Configuration for vyos Router 2. |
|   `ISP1config.boot`  | Configuration for vyos Router 1. |

## Purpose

These configuration files are used to deploy and verify the network topology. They include interface settings, routing protocols, IP addressing, and management configurations.

## Requirements

- Compatible device or network emulator (e.g., EVE-NG, GNS3, Cisco CML)
- Appropriate network operating system version
- SSH or console access for deployment

## Usage

1. Load the configuration onto the appropriate device.
2. Verify interface status.
3. Confirm IP connectivity.
4. Test routing and network services.

## Notes

- Review IP addresses before deploying in a production environment.
- Update passwords and management settings as needed.
- Ensure all referenced devices are running compatible software versions.
