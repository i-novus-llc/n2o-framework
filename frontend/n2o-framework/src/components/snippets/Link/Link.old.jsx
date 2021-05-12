import React from 'react'
import PropTypes from 'prop-types'

import history from '../../../history'

/**
 * Компонент ссылки с сохранением в объект history
 * @reactProps {string} to - путь куда следует перейти
 * @reactProps {function} onClick - callback нажатия на ссылку
 * @example
 * <Link to="/path/1" onClick={this.changeUrl}>Ссылка</Link>
 */
export class Link extends React.Component {
    /**
     * Обработчик нажатия
     * @param event
     */
    handleClick(event) {
        const { onClick, to } = this.props

        if (onClick) {
            onClick(event)
        }

        if (event.button !== 0 /* left click */) {
            return
        }

        if (event.metaKey || event.altKey || event.ctrlKey || event.shiftKey) {
            return
        }

        if (event.defaultPrevented === true) {
            return
        }

        event.preventDefault()

        if (to) {
            history.push(to)
        } else {
            history.push({
                pathname: event.currentTarget.pathname,
                search: event.currentTarget.search,
            })
        }
    }

    render() {
        const { to, ...props } = this.props

        return (
            // eslint-disable-next-line jsx-a11y/anchor-has-content
            <a
                href={typeof to === 'string' ? to : history.createHref(to)}
                {...props}
                onClick={this.handleClick}
            />
        )
    }
}

Link.propTypes = {
    to: PropTypes.oneOfType([PropTypes.string, PropTypes.object]).isRequired,
    onClick: PropTypes.func,
}

export default Link
