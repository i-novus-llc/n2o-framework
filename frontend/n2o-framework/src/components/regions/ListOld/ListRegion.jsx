import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'

import withWidgetProps from '../withWidgetProps'
import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import SecurityCheck from '../../../core/auth/SecurityCheck'

import ListItem from './ListItem'
import ListOld from './List'

/**
 * Регион Лист
 * @reactProps {array} items - массив из объектов, которые описывают виджет{id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} getWidget - функция получения виджета
 */

class ListRegion extends React.Component {
    /**
   * Рендер
   */
    render() {
        const { items, getWidget, pageId } = this.props

        return (
            <ListOld>
                {items.map((item) => {
                    const listItem = (
                        <ListItem
                            key={item.widgetId}
                            id={item.widgetId}
                            title={item.label || item.widgetId}
                            active={item.opened}
                        >
                            <Factory
                                containerId={item.widgetId}
                                pageId={pageId}
                                level={WIDGETS}
                                fetchOnInit={item.fetchOnInit}
                                id={item.widgetId}
                                {...getWidget(pageId, item.widgetId)}
                            />
                        </ListItem>
                    )
                    const { security } = item

                    return isEmpty(security) ? (
                        listItem
                    ) : (
                        <SecurityCheck
                            config={security}
                            active={item.opened}
                            id={item.widgetId}
                            render={({ permissions, onClick, active }) => (permissions
                                ? React.cloneElement(listItem, { onClick, active })
                                : null)
                            }
                        />
                    )
                })}
            </ListOld>
        )
    }
}

ListRegion.propTypes = {
    items: PropTypes.array.isRequired,
    getWidget: PropTypes.func.isRequired,
    pageId: PropTypes.string.isRequired,
}

export default withWidgetProps(ListRegion)
