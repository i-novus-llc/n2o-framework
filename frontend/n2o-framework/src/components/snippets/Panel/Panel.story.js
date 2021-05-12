import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'
import { SNIPPETS } from '../../../core/factory/factoryLevels'

import Panel from './PanelShortHand'
import PanelJson from './Panel.meta.json'

const stories = storiesOf('Регионы/Панель', module)

stories.addParameters({
    info: {
        propTablesExclude: [Factory],
    },
})

const tabs = [
    {
        id: '1',
        header: 'Первый таб',
        content: <h1>Контент первого таба</h1>,
    },
    {
        id: '2',
        header: 'Второй таб',
        content: <h1>Контент второго таба</h1>,
    },
]

stories
    .add('Компонент', () => {
        const props = {
            className: PanelJson.className,
            color: PanelJson.color,
            icon: PanelJson.icon,
            hasTabs: PanelJson.hasTabs,
            tabs,
            headerTitle: PanelJson.headerTitle,
            footerTitle: PanelJson.footerTitle,
            open: PanelJson.open,
            collapsible: PanelJson.collapsible,
            fullScreen: PanelJson.fullScreen,
            containers: PanelJson.containers,
        }

        return (
            <Panel {...props}>
                <h1>Содержимое панели</h1>
            </Panel>
        )
    })
    .add('Создание через Factory', () => {
        const dt = {
            id: 'uniqId',
            src: 'Panel',
            tabs,
            className: PanelJson.className,
            color: PanelJson.color,
            icon: PanelJson.icon,
            hasTabs: true,
            text: 'text',
            headerTitle: PanelJson.headerTitle,
            footerTitle: PanelJson.footerTitle,
            open: PanelJson.open,
            collapsible: PanelJson.collapsible,
            fullScreen: PanelJson.fullScreen,
            containers: PanelJson.containers,
        }
        return (
            <>
                <Factory level={SNIPPETS} id="uniqId" {...dt} />
            </>
        )
    })
