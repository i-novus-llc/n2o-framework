import React from 'react'
import some from 'lodash/some'
import isNil from 'lodash/isNil'
import isArray from 'lodash/isArray'
import map from 'lodash/map'
import flowRight from 'lodash/flowRight'

import PanelShortHand from '../../snippets/Panel/PanelShortHand'
import { createRegionContainer } from '../withRegionContainer'
import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'

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

class PanelRegion extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            tabs: [],
        }
        this.checkPanel = this.checkPanel.bind(this)
        this.getTab = this.getTab.bind(this)
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps() { this.getPanelsWithAccess() }

    componentDidMount() { this.getPanelsWithAccess() }

    getContent = (meta, pageId) => {
        const content = isArray(meta) ? meta : [meta]

        return <RegionContent content={content} pageId={pageId} />
    }

    getTab(panel) {
        const { getWidget, pageId } = this.props

        return {
            id: panel.widgetId,
            content: this.getContent(panel, pageId),
            header: panel.label,
            ...panel,
            ...getWidget(pageId, panel.widgetId),
        }
    }

    // eslint-disable-next-line @typescript-eslint/require-await
    async checkPanel(panel) {
        const { tabs } = this.state

        this.setState({ tabs: tabs.concat(this.getTab(panel)) })
    }

    getPanelsWithAccess() {
        const { content } = this.props

        this.setState({ tabs: [] }, async () => {
            if (content && typeof content[Symbol.iterator] === 'function') {
                for (const panel of content) {
                    await this.checkPanel(panel)
                }
            }
        })
    }

    render() {
        const {
            content,
            getWidgetProps,
            activeEntity,
            open,
            changeActiveEntity,
            className,
            style,
            pageId,
        } = this.props
        const { tabs } = this.state
        const visible = some(content, item => getWidgetProps(item.id).visible === true)

        return (
            <PanelShortHand
                tabs={tabs}
                {...this.props}
                open={!isNil(activeEntity) ? activeEntity : open}
                style={{ display: !visible && 'none', ...style }}
                onVisibilityChange={changeActiveEntity}
                className={className}
            >
                {map(content, meta => this.getContent(meta, pageId))}
            </PanelShortHand>
        )
    }
}

PanelRegion.defaultProps = {
    open: true,
    collapsible: false,
    hasTabs: false,
    fullScreen: false,
}

export { PanelRegion }
export default flowRight(
    createRegionContainer({ listKey: 'panels' }),
    withWidgetProps,
)(PanelRegion)
