import React from 'react'
import PropTypes from 'prop-types'
import every from 'lodash/every'
import isUndefined from 'lodash/isUndefined'
import isArray from 'lodash/isArray'
import map from 'lodash/map'
import { compose, setDisplayName } from 'recompose'

import PanelShortHand from '../../snippets/Panel/PanelShortHand'
import withRegionContainer from '../withRegionContainer'
import withWidgetProps from '../withWidgetProps'
import withSecurity from '../../../core/auth/withSecurity'
import { SECURITY_CHECK } from '../../../core/auth/authTypes'
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
    componentWillReceiveProps() {
        this.getPanelsWithAccess()
    }

    componentDidMount() {
        this.getPanelsWithAccess()
    }

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

    async checkPanel(panel) {
        const { tabs } = this.state

        if (panel.security) {
            const { user, authProvider } = this.props
            const config = panel.security

            try {
                await authProvider(SECURITY_CHECK, {
                    config,
                    user,
                })

                this.setState({ tabs: tabs.concat(this.getTab(panel)) })
            } catch (error) {
                // ...
            }
        } else {
            this.setState({ tabs: tabs.concat(this.getTab(panel)) })
        }
    }

    getPanelsWithAccess() {
        const { content } = this.props

        this.setState({ tabs: [] }, async () => {
            // eslint-disable-next-line no-restricted-syntax
            for (const panel of content) {
                await this.checkPanel(panel)
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
        const isInvisible = every(
            content,
            item => getWidgetProps(item.id).visible === false,
        )

        return (
            <PanelShortHand
                tabs={tabs}
                {...this.props}
                open={isUndefined(activeEntity) ? open : activeEntity}
                style={{ display: isInvisible && 'none', ...style }}
                onVisibilityChange={changeActiveEntity}
                className={className}
            >
                {map(content, meta => this.getContent(meta, pageId))}
            </PanelShortHand>
        )
    }
}

PanelRegion.propTypes = {
    /**
     * Список элементов
     */
    content: PropTypes.array.isRequired,
    /**
     * ID страницы
     */
    pageId: PropTypes.string.isRequired,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Стили
     */
    style: PropTypes.object,
    /**
     * Цвет панели
     */
    color: PropTypes.string,
    /** *
     * Иконка панели
     */
    icon: PropTypes.string,
    /**
     * Текст заголовка
     */
    headerTitle: PropTypes.string,
    /**
     * Текст футера
     */
    footerTitle: PropTypes.string,
    /**
     * Флаг открытия панели
     */
    open: PropTypes.bool,
    /**
     * Флаг возможности скрывать содержимое панели
     */
    collapsible: PropTypes.bool,
    /**
     * Флаг наличия табов
     */
    hasTabs: PropTypes.bool,
    /**
     * Флаг открытия на весь экран
     */
    fullScreen: PropTypes.bool,
    getWidget: PropTypes.func.isRequired,
    resolveVisibleDependency: PropTypes.func,
    dependency: PropTypes.object,
    getWidgetProps: PropTypes.func,
    changeActiveEntity: PropTypes.func,
    authProvider: PropTypes.func,
    activeEntity: PropTypes.any,
    user: PropTypes.any,
}

PanelRegion.defaultProps = {
    open: true,
    collapsible: false,
    hasTabs: false,
    fullScreen: false,
}

export { PanelRegion }
export default compose(
    setDisplayName('PanelRegion'),
    withRegionContainer({ listKey: 'panels' }),
    withSecurity,
    withWidgetProps,
)(PanelRegion)
