import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import get from 'lodash/get'
import some from 'lodash/some'
import { compose, setDisplayName, withProps } from 'recompose'
import classNames from 'classnames'

import { makeRegionTabsSelector } from '../../../ducks/regions/store'
import { SecurityController, Behavior } from '../../../core/auth/SecurityController'
import withRegionContainer from '../withRegionContainer'
import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { WithDataSource } from '../../../core/widget/WithDataSource'

import Tabs from './Tabs'
import { Tab } from './Tab'

/**
 * Регион Таб
 * @reactProps {array} tabs - массив из объектов, которые описывают виджет {id, name, opened, pageId, widget}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} resolveVisibleDependency - резол видимости таба
 * @reactProps {function} hideSingleTab - скрывать / не скрывать навигацию таба, если он единственный
 */

/* TODO компоненту требуется рефакторинг */

class TabRegion extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            // eslint-disable-next-line react/no-unused-state
            readyTabs: this.findReadyTabs(),
            permissionsVisibleTabs: {},
        }
        this.handleChangeActive = this.handleChangeActive.bind(this)
    }

    componentDidMount() {
        const { datasource, activeTabFieldId } = this.props

        if (datasource && activeTabFieldId) {
            const { setResolve, activeEntity, models = {} } = this.props
            const { resolve } = models

            setResolve({ ...resolve, [activeTabFieldId]: activeEntity })
        }
    }

    componentDidUpdate(prevProps) {
        const { activeEntity: prevActiveEntity } = prevProps
        const { activeEntity, datasource, changeActiveEntity } = this.props

        if (activeEntity !== prevActiveEntity) {
            return
        }

        if (datasource) {
            const { activeTabFieldId, models = {} } = this.props

            if (!activeTabFieldId) {
                // eslint-disable-next-line no-console
                console.warn(`TabsRegion Error: datasource with id equal to ${datasource} does not contain activeTabFieldId`)

                return
            }

            const { resolve } = models

            const activeFromResolve = get(resolve, activeTabFieldId, null)

            /* FIXME
               нужно пересмотреть зависимость activeEntity от resolve model,
               существуют конфликты с авто переключением
            */
            if (activeFromResolve !== activeEntity) {
                changeActiveEntity(activeFromResolve)
            }
        }
    }

    componentWillUnmount() {
        const { changeActiveEntity } = this.props

        changeActiveEntity(null)
    }

    onPermissionsSet = (tabId, hasAccess) => {
        this.setState(prevState => ({
            permissionsVisibleTabs: {
                ...prevState.permissionsVisibleTabs,
                [tabId]: hasAccess,
            },
        }))
    }

    handleChangeActive(event, id) {
        const { changeActiveEntity, datasource, activeTabFieldId } = this.props

        changeActiveEntity(id)

        if (datasource && activeTabFieldId) {
            const { setResolve, models = {} } = this.props
            const { resolve } = models

            setResolve({ ...resolve, [activeTabFieldId]: id })
        }
    }

    findReadyTabs() {
        const { tabs } = this.props

        return map(tabs, tab => tab.id)
    }

    isVisibleWidget(id) {
        const { getWidgetProps } = this.props
        const widgetProps = getWidgetProps(id)

        return get(widgetProps, 'visible')
    }

    atLeastOneVisibleWidget(content) {
        const nestedMetaKeys = ['content', 'menu', 'tabs']

        return some(content, (meta) => {
            for (const key of nestedMetaKeys) {
                if (meta[key]) {
                    return this.atLeastOneVisibleWidget(meta[key])
                }
            }

            return this.isVisibleWidget(meta.id)
        })
    }

    tabVisible(tab) {
        const content = get(tab, 'content')

        return this.atLeastOneVisibleWidget(content)
    }

    regionVisible(tabs) {
        return some(tabs, tab => this.tabVisible(tab))
    }

    render() {
        const {
            tabs,
            activeEntity,
            className,
            hideSingleTab,
            maxHeight,
            scrollbar,
            title,
            style,
            pageId,
            disabled,
        } = this.props

        const { permissionsVisibleTabs } = this.state

        return (
            <div
                className={classNames(
                    'n2o-tabs-region',
                    className, {
                        visible: this.regionVisible(tabs),
                        'n2o-disabled': disabled,
                    },
                )}
                style={style}
            >
                <Tabs
                    activeId={activeEntity}
                    onChangeActive={this.handleChangeActive}
                    hideSingleTab={hideSingleTab}
                    dependencyVisible={this.regionVisible(tabs)}
                    maxHeight={maxHeight}
                    scrollbar={scrollbar}
                    title={title}
                >
                    {map(tabs, (tab) => {
                        const { security, content } = tab
                        const behaviorDisable = security?.behavior === Behavior.DISABLE
                        const tabHasAccess = get(permissionsVisibleTabs, tab.id, true)
                        const visible = behaviorDisable || (tabHasAccess && this.tabVisible(tab))

                        const tabProps = {
                            key: tab.id,
                            id: tab.id,
                            title: tab.label || tab.widgetId,
                            icon: tab.icon,
                            active: tab.opened,
                            invalid: tab.invalid,
                            visible,
                            disabled: behaviorDisable && !tabHasAccess,
                        }

                        const tabElement = (
                            <Tab {...tabProps}>
                                <RegionContent
                                    content={content}
                                    pageId={pageId}
                                    tabSubContentClass="tab-sub-content"
                                />
                            </Tab>
                        )

                        return isEmpty(security) ? (
                            tabElement
                        ) : (
                            <SecurityController
                                {...tabProps}
                                config={security}
                                onPermissionsSet={hasAccess => this.onPermissionsSet(tab.id, hasAccess)}
                            >
                                {tabElement}
                            </SecurityController>
                        )
                    })}
                </Tabs>
            </div>
        )
    }
}

TabRegion.propTypes = {
    style: PropTypes.object,
    /**
     * Список табов
     */
    tabs: PropTypes.array.isRequired,
    getWidget: PropTypes.func.isRequired,
    /**
     * контент Tab, (регион или виджет)
     */
    content: PropTypes.array,
    /**
     * ID странцы
     */
    pageId: PropTypes.string.isRequired,
    alwaysRefresh: PropTypes.bool,
    mode: PropTypes.oneOf(['single', 'all']),
    /**
     * Флаг ленивого рендера
     */
    lazy: PropTypes.bool,
    resolveVisibleDependency: PropTypes.func,
    changeActiveEntity: PropTypes.func,
    getWidgetProps: PropTypes.func,
    hideSingleTab: PropTypes.bool,
    activeEntity: PropTypes.any,
    disabled: PropTypes.bool,
    className: PropTypes.string,
    title: PropTypes.string,
    maxHeight: PropTypes.number,
    scrollbar: PropTypes.any,
    datasource: PropTypes.string,
    activeTabFieldId: PropTypes.string,
    setResolve: PropTypes.func,
    models: PropTypes.object,
}

TabRegion.defaultProps = {
    alwaysRefresh: false,
    lazy: false,
    mode: 'single',
    hideSingleTab: false,
}

const mapStateToProps = (state, props) => ({
    tabs: makeRegionTabsSelector(props.id)(state),
})

export { TabRegion }
export default compose(
    setDisplayName('TabsRegion'),
    withRegionContainer({ listKey: 'tabs' }),
    connect(mapStateToProps),
    withWidgetProps,
    withProps(() => ({
        widget: false,
    })),
    WithDataSource,
)(TabRegion)
