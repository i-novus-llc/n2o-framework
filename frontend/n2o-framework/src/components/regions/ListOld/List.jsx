import React from 'react'
import PropTypes from 'prop-types'
import includes from 'lodash/includes'
import pull from 'lodash/pull'
import map from 'lodash/map'
import filter from 'lodash/filter'

/**
 * Компонент Список
 * @reactProps {string} className - css-класс
 * @reactProps {boolean} animation
 * @reactProps {function} onSelect
 * @reactProps {node} children - элемент потомок компонента List
 * @example
 *  <List>
 * {
 *  containers.map((cnt) =>
 *    <ListItem id={cnt.id}
 *              title={cnt.name || cnt.id}
 *              active={cnt.opened}>
 *      <WidgetFactory containerId={cnt.id} pageId={cnt.pageId} fetchOnInit={cnt.fetchOnInit} {...cnt.widget} />
 *    </ListItem>
 *  )
 *}
 * </List>
 */

class List extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            activeIds: List.defaultOpenedId(props.children),
        }

        this.handleChangeActive = this.handleChangeActive.bind(this)
    }

    /**
   * Установка нового активного эдмента списка
   * @param e
   * @param id
   */
    handleChangeActive(e, id) {
        let oldIds = Object.assign([], this.state.activeIds)

        if (includes(oldIds, id)) {
            pull(oldIds, id)
        } else {
            oldIds = oldIds.concat(id)
        }
        this.setState({
            activeIds: oldIds,
        })
    }

    /**
   * getter для айдишников открытых по умолчанию элементов списка
   */
    static defaultOpenedId(children) {
        return map(
            filter(React.Children.toArray(children), child => child.props.active),
            child => child.props.id,
        )
    }

    /**
   * getter для пропсов дочернего компонента
   */
    getChildProps(child) {
        const { activeIds } = this.state

        return {
            active: includes(activeIds, child.props.id),
            onClick: this.handleChangeActive,
        }
    }

    render() {
        const { className, children } = this.props

        return (
            <div className={className} style={{ marginBottom: 2 }}>
                {React.Children.map(children, child => React.cloneElement(child, this.getChildProps(child)))}
            </div>
        )
    }
}

List.propTypes = {
    className: PropTypes.string,
    animation: PropTypes.bool,
    onSelect: PropTypes.func,
    children: PropTypes.node,
}

export default List
