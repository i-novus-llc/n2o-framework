import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import Nav from 'reactstrap/lib/Nav'
import NavItem from 'reactstrap/lib/NavItem'
import NavLink from 'reactstrap/lib/NavLink'
import cx from 'classnames'

/**
 * Контролл Pills
 * @param data - данные
 * @param rest
 * @returns {*}
 * @constructor
 */
function Pills({ data, onClick, className, ...rest }) {
    const handleOnClick = id => (e) => {
        onClick(e, id)
    }

    const renderPills = ({ id, label, active }) => (
        <NavItem>
            <NavLink href="#" active={active} onClick={handleOnClick(id)}>
                {label}
            </NavLink>
        </NavItem>
    )

    return (
        <Nav className={cx('n2o-pill-filter', className)} pills {...rest}>
            {map(data, renderPills)}
        </Nav>
    )
}

Pills.propTypes = {
    data: PropTypes.array,
    className: PropTypes.string,
}
Pills.defaultProps = {
    multiSelect: [],
}

export default Pills
