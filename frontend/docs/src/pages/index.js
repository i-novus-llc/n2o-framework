import React from 'react'
import clsx from 'clsx'
import Layout from '@theme/Layout'
import Link from '@docusaurus/Link'
import useDocusaurusContext from '@docusaurus/useDocusaurusContext'
import useThemeContext from '@theme/hooks/useThemeContext'
import useBaseUrl from '@docusaurus/useBaseUrl'

import { Tabs } from '../components/Tabs/Tabs'
import { Sandbox } from '../components/Sandbox/Sandbox'
import { Cards } from '../components/Cards/Cards'
import { features, tabsValues, projectIds, cardItems } from '../constants/constants'

import styles from './styles.module.css'

function Feature({ imageUrl, title, description, index }) {
    const imgUrl = useBaseUrl(imageUrl)
    const isFeatureReverse = index % 2 === 0

    return (
        <div className={clsx(styles.feature, {[styles.featureReverse] : isFeatureReverse})}>

            {imgUrl && (
                <div className="text--center">
                    <img className={styles.featureImage} src={imgUrl} alt={title}/>
                </div>
            )}
            <div className={styles.featureDescriptionBlock}>
                <h3 className={styles.featureTitle}>{title}</h3>
                <p className={styles.featureDescription}>{description}</p>
            </div>

        </div>
    )
}

function TabComponent({ value }) {
    const projectId = projectIds[value]

    return (
        <div className={styles.sandboxWrapper}>
            <Sandbox
                customStyle={{border: 'none'}}
                projectId={projectId}
                className={styles.sandboxLightEditor}
                isLightEditor
            />
        </div>
    )
}

function Page() {
    const context = useDocusaurusContext()
    const { siteConfig = {} } = context
    const { isDarkTheme } = useThemeContext();

    return (
        <>
            <header className={clsx('hero', styles.heroBanner)}>
                <div className="container">
                    <h1 className={clsx('hero__title', styles.mainTitle)}>
                        {siteConfig.title}
                    </h1>
                    <p className={clsx('hero__subtitle', styles.mainSubTitle)}>
                        {siteConfig.tagline}
                    </p>
                    <div className={styles.buttons}>
                        <Link
                            className={clsx(
                                styles.getStarted,
                            )}
                            to={useBaseUrl('docs/')}>
                            Начать работу
                        </Link>
                    </div>
                </div>
            </header>
            <main>
                {features && features.length > 0 && (
                    <section className={styles.features}>
                        <div className="container">
                            <div className="row">
                                {features.map((props, idx) => (
                                    <Feature key={idx} index={idx} {...props} />
                                ))}
                            </div>
                        </div>
                    </section>
                )}
            </main>
            <section className={styles.examplesTabsWrapper}>
                <div className={styles.tabsSection}>
                    <Tabs
                        values={tabsValues}
                        className={isDarkTheme ? styles.examplesTabsDark : styles.examplesTabs}
                        Component={TabComponent}
                    />
                </div>
            </section>
            <section className={styles.getStartedCards}>
                <Cards items={cardItems} className={styles.getStartedCard} />
            </section>
        </>
    )
}

export default function Home(props) {
    return (
        <Layout>
            <Page {...props} />
        </Layout>
    )
}
