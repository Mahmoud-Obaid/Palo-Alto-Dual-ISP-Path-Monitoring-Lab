interfaces {
    ethernet eth0 {
        address dhcp
        duplex auto
        hw-id 00:0c:29:35:37:f2
        smp_affinity auto
        speed auto
    }
    ethernet eth1 {
        address 10.10.10.10/24
        duplex auto
        hw-id 00:0c:29:35:37:fc
        smp_affinity auto
        speed auto
    }
    loopback lo {
    }
}
nat {
    source {
        rule 100 {
            outbound-interface eth0
            source {
                address 10.10.10.0/24
            }
            translation {
                address masquerade
            }
        }
    }
}
protocols {
    static {
        route 0.0.0.0/0 {
            next-hop 192.168.1.1 {
            }
        }
    }
}
service {
    ssh {
        listen-address 10.10.10.10
        port 22
    }
}
system {
    config-management {
        commit-revisions 100
    }
    console {
    }
    host-name vyos
    login {
        user vyos {
            authentication {
                encrypted-password $1$gU100Pg/$xQUpNRtppcSQuD6bKQkGI1
                plaintext-password ""
            }
            level admin
        }
    }
    ntp {
        server 0.pool.ntp.org {
        }
        server 1.pool.ntp.org {
        }
        server 2.pool.ntp.org {
        }
    }
    package {
        auto-sync 1
        repository community {
            components main
            distribution helium
            password ""
            url http://packages.vyos.net/vyos
            username ""
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "cluster@1:config-management@1:conntrack-sync@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@4:nat@4:qos@1:quagga@2:system@6:vrrp@1:wanloadbalance@3:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: VyOS 1.1.8 */
