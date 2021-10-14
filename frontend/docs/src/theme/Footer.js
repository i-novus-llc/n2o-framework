import React from 'react'
import CONFIG from '../ci-config.json'

import styles from './styles.module.css'
import N2oLogo from '../../static/img/n2o_logo_light.svg'

export default function Footer() {
    return (
        <footer className={styles.footer}>
            <section className={styles.footerLogoSection}>
                <N2oLogo/>
                <div className={styles.footerBrand}>N2O Framework</div>
            </section>
            <div>{`Copyright © ${new Date().getFullYear()} N2O, Inc. Built with I-Novus.`}</div>
            <div>{CONFIG.n2oVersion}</div>
        </footer>
    )
}
