import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import get from 'lodash/get'
import some from 'lodash/some'
import { compose, setDisplayName } from 'recompose'
import classNames from 'classnames'

import SecurityCheck from '../../../core/auth/SecurityCheck'
import withRegionContainer from '../withRegionContainer'
import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'

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

    componentWillUnmount() {
        const { changeActiveEntity } = this.props

        changeActiveEntity(null)
    }

    handleChangeActive(event, id) {
        const { changeActiveEntity } = this.props

        changeActiveEntity(id)
    }

    findReadyTabs() {
        const { tabs } = this.props

        return map(tabs, tab => tab.id)
    }

    isVisibleWidget(id) {
        const { getWidgetProps } = this.props
        const widgetProps = getWidgetProps(id)

        return get(widgetProps, 'isVisible')
    }

    atLeastOneVisibleWidget(content) {
        return some(content, (meta) => {
            if (meta.content) {
                return this.atLeastOneVisibleWidget(meta.content)
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
        } = this.props

        const { permissionsVisibleTabs } = this.state

        return (
            <div
                className={classNames(
                    'n2o-tabs-region',
                    className, {
                        visible: this.regionVisible(tabs),
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
                        const permissionVisible = get(permissionsVisibleTabs, tab.id, true)
                        const visible = permissionVisible && this.tabVisible(tab)
                        const tabProps = {
                            key: tab.id,
                            id: tab.id,
                            title: tab.label || tab.widgetId,
                            icon: tab.icon,
                            active: tab.opened,
                            visible,
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
                        const onPermissionsSet = (permissions) => {
                            this.setState(prevState => ({
                                visibleTabs: {
                                    ...prevState.visibleTabs,
                                    [tab.id]: !!permissions,
                                },
                            }))
                        }

                        return isEmpty(security) ? (
                            tabElement
                        ) : (
                            <SecurityCheck
                                {...tabProps}
                                config={security}
                                onPermissionsSet={onPermissionsSet}
                                render={({ permissions, active, visible }) => (permissions
                                    ? React.cloneElement(tabElement, { active, visible })
                                    : null)}
                            />
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
    className: PropTypes.string,
    title: PropTypes.string,
    maxHeight: PropTypes.number,
    scrollbar: PropTypes.any,
}

TabRegion.defaultProps = {
    alwaysRefresh: false,
    lazy: false,
    mode: 'single',
    hideSingleTab: false,
}

export { TabRegion }
export default compose(
    setDisplayName('TabsRegion'),
    withRegionContainer({ listKey: 'tabs' }),
    withWidgetProps,
)(TabRegion)
