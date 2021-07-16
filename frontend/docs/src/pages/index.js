import React from 'react'
import clsx from 'clsx'
import Layout from '@theme/Layout'
import Link from '@docusaurus/Link'
import useDocusaurusContext from '@docusaurus/useDocusaurusContext'
import useBaseUrl from '@docusaurus/useBaseUrl'

import styles from './styles.module.css'

const features = [
    {
        title: 'Реактивный',
        imageUrl: 'img/undraw_docusaurus_mountain.svg',
        description: (
            <>
                Создавать интерактивные приложения на N2O можно легко и быстро как в конструкторе.
                Для этого Вам не потребуются глубокие знания языков программирования.
                Разработка происходит в декларативном стиле на языке XML.
                Подключение к данным легко реализуется через SQL, REST, Spring и другие технологии.
            </>
        ),
    },
    {
        title: 'Мощный',
        imageUrl: 'img/undraw_docusaurus_tree.svg',
        description: (
            <>
                N2O имеет встроенную библиотеку React компонентов с настраиваемой вёрсткой и поведением.
                С их помощью Вы можете компоновать страницу как хотите.
                N2O поддерживает условия видимости и доступности полей, кнопок, панелей; фильтрации, сортировки данных;
                открытия ссылок, вложенных страниц, модальных окон; валидации, сохранения и печати данных.
            </>
        ),
    },
    {
        title: 'Открытый',
        imageUrl: 'img/undraw_docusaurus_react.svg',
        description: (
            <>
                N2O Framework - библиотека с открытым исходным кодом,
                Вы можете использовать её свободно в любых проектах.
                N2O - компонентный фреймворк и его можно расширить под любые требования:
                создать свою тему стилей; добавить специфические React компоненты;
                реализовать новый способ получения данных.
                В N2O есть механизмы быстрого подключения системы авторизации, журналирования, аудита и отчетов.
            </>
        ),
    },
]

function Feature({ imageUrl, title, description }) {
    const imgUrl = useBaseUrl(imageUrl)
    return (
        <div className={clsx('col col--4', styles.feature)}>
            {imgUrl && (
                <div className="text--center">
                    <img className={styles.featureImage} src={imgUrl} alt={title}/>
                </div>
            )}
            <h3>{title}</h3>
            <p>{description}</p>
        </div>
    )
}

export default function Home() {
    const context = useDocusaurusContext()
    const { siteConfig = {} } = context
    return (
        <Layout
            title={`Hello from ${siteConfig.title}`}
            description="Description will go into a meta tag in <head />">
            <header className={clsx('hero hero--primary', styles.heroBanner)}>
                <div className="container">
                    <h1 className="hero__title">{siteConfig.title}</h1>
                    <p className="hero__subtitle">{siteConfig.tagline}</p>
                    <div className={styles.buttons}>
                        <Link
                            className={clsx(
                                'button button--outline button--secondary button--lg',
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
                                    <Feature key={idx} {...props} />
                                ))}
                            </div>
                        </div>
                    </section>
                )}
            </main>
        </Layout>
    )
}
