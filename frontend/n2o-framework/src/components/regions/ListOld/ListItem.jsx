/**
 * Created by emamoshin on 10.10.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

/**
 * Элемент {@link List}
 * @reactProps {number} id - id элемента
 * @reactProps {boolean} active - флаг активноти ListItem
 * @reactProps {string} title - тайтл ListItem
 * @reactProps {function} onClick - обработка клика по тайтлу
 * @reactProps {node} children - элемент потомок ListItem
 * @example
 * <ListItem id={cnt.id}
 *   title={cnt.name || cnt.id}
 *   active={cnt.opened}>
 *     <WidgetFactory containerId={cnt.id} pageId={cnt.pageId} fetchOnInit={cnt.fetchOnInit} {...cnt.widget} />
 * </ListItem>
 */
class ListItem extends React.Component {
    constructor(props) {
        super(props)
        this.handleClick = this.handleClick.bind(this)
        this.state = {
            wasActive: false,
        }
    }

    static getDerivedStateFromProps(nextProps) {
        if (nextProps.active) {
            return {
                wasActive: true,
            }
        }
    }

    /**
   * Обработка клика
   * @param e
   */
    handleClick(e) {
        const { id, onClick } = this.props
        e.preventDefault()
        if (onClick) {
            onClick(e, id)
        }
    }

    /**
   *  базовый рендер
   */
    render() {
        const { active, title, children } = this.props
        const { wasActive } = this.state
        return (
            <div className="n2o-region-list" style={{ marginBottom: 2 }}>
                <div className={cx('n2o-region-list__header', { active })}>
                    <a href="#" onClick={this.handleClick}>
                        <h6>
                            <span
                                className="fa fa-angle-right"
                                style={{ marginRight: '0.25rem' }}
                            />
                            {' '}
                            {title}
                        </h6>
                    </a>
                </div>
                <div className={cx('n2o-region-list__body', { active })}>
                    {wasActive ? children : null}
                </div>
            </div>
        )
    }
}

ListItem.propTypes = {
    id: PropTypes.string,
    active: PropTypes.bool,
    title: PropTypes.string,
    onClick: PropTypes.func,
    children: PropTypes.node,
}

export default ListItem
