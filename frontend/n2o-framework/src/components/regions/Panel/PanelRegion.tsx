import React, { Component } from 'react'
import isNil from 'lodash/isNil'
import flowRight from 'lodash/flowRight'

import { PanelContainer } from '../../snippets/Panel/PanelContainer'
import { createRegionContainer } from '../withRegionContainer'
import withWidgetProps, { WithGetWidgetProps } from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { type PanelContainerProps, PanelTab } from '../../snippets/Panel/types'
import { ContentMeta } from '../../../ducks/regions/Regions'

export interface State {
    tabs: PanelContainerProps['tabs']
}

export type PanelRegionProps = PanelContainerProps & WithGetWidgetProps & {
    getWidget(pageId: string, widgetId: string): Record<string, unknown>
    pageId: string
    content: PanelContainerProps['tabs']
    activeEntity?: boolean
    changeActiveEntity: PanelContainerProps['onVisibilityChange']
    getWidgetProps(id: PanelTab['id']): { visible?: boolean }
}

function hasVisibleItem(
    items: PanelTab[] | undefined,
    getWidgetProps: (id: string) => { visible?: boolean },
): boolean {
    if (!Array.isArray(items)) { return false }

    return items.some((item) => {
        if (item.id && getWidgetProps(item.id).visible === true) { return true }
        if (item.content && hasVisibleItem(item.content, getWidgetProps)) { return true }

        return !!(item.tabs && hasVisibleItem(item.tabs, getWidgetProps))
    })
}

/**
 * Регион Панель
 * @reactProps containers {array} - массив из объектов, которые описывают виджет {id, name, opened, pageId, widget}
 * @reactProps className (string) - имя класса для родительского элементаs
 * @reactProps style (object) - стили для родительского элемента
 * @reactProps color (string) - стиль для панели
 * @reactProps icon (string) - класс для иконки
 * @reactProps headerTitle (string) - заголовок для шапки
 * @reactProps footerTitle (string) - заголовок для футера
 * @reactProps collapsible (boolean) - флаг возможности скрывать содержимое панели
 * @reactProps open (boolean) - флаг открытости панели
 * @reactProps hasTabs (boolean) - флаг наличия табов
 * @reactProps fullScreen (boolean) - флаг возможности открывать на полный экран
 * @reactProps {array} content - массив панелей вида
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {object} user - пользователь !!! не используется
 * @reactProps {function} authProvider - провайдер аутентификации !!! не используется
 * @reactProps {function} resolveVisibleDependency - резол видимости региона
 * @reactProps {object} dependency - зависимость видимости панели
 */

// TODO необходим рефакторинг компонента и типизации
class PanelRegionBody extends Component<PanelRegionProps, State> {
    constructor(props: PanelRegionProps) {
        super(props)
        this.state = { tabs: [] }

        this.checkPanel = this.checkPanel.bind(this)
        this.getTab = this.getTab.bind(this)
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps() { this.getPanelsWithAccess() }

    componentDidMount() { this.getPanelsWithAccess() }

    getContent = (meta: PanelTab, pageId: string) => {
        const content = Array.isArray(meta) ? meta : [meta]

        return <RegionContent content={content as ContentMeta[]} pageId={pageId} />
    }

    getTab(panel: PanelTab) {
        const { getWidget, pageId } = this.props

        return {
            ...panel,
            id: panel.widgetId,
            content: this.getContent(panel, pageId),
            header: panel.label,
            ...getWidget(pageId, panel.widgetId),
        } as unknown as PanelTab
    }

    checkPanel(panel: PanelTab) {
        const { tabs } = this.state

        this.setState({
            tabs: tabs.concat(this.getTab(panel)),
        })
    }

    getPanelsWithAccess() {
        const { content } = this.props

        this.setState({ tabs: [] }, () => {
            if (content && typeof content[Symbol.iterator] === 'function') {
                for (const panel of content) {
                    this.checkPanel(panel)
                }
            }
        })
    }

    render() {
        const {
            getWidgetProps,
            activeEntity,
            open,
            changeActiveEntity,
            className,
            pageId,
            style: propsStyle,
            content = [],
        } = this.props
        const { tabs } = this.state
        const visible = hasVisibleItem(content, getWidgetProps)
        const style = !visible ? { display: 'none', ...propsStyle } : propsStyle

        return (
            <PanelContainer
                {...this.props}
                tabs={tabs}
                open={!isNil(activeEntity) ? activeEntity : open}
                style={style}
                onVisibilityChange={changeActiveEntity}
                className={className}
            >
                {content.map(meta => this.getContent(meta, pageId))}
            </PanelContainer>
        )
    }

    static defaultProps = {
        open: true,
        collapsible: false,
        hasTabs: false,
        fullScreen: false,
    } as PanelRegionProps
}

export const PanelRegion = flowRight(
    createRegionContainer({ listKey: 'panels' }),
    withWidgetProps<PanelRegionProps>,
)(PanelRegionBody)

export default PanelRegion
