import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import Nav from 'reactstrap/lib/Nav'
import NavItem from 'reactstrap/lib/NavItem'
import NavLink from 'reactstrap/lib/NavLink'
import classNames from 'classnames'

/**
 * Контролл Pills
 * @constructor
 */
function Pills({ data, onClick, className, ...rest }) {
    const handleOnClick = id => (e) => {
        onClick(e, id)
    }

    // eslint-disable-next-line react/prop-types
    const renderPills = ({ id, label, active }) => (
        <NavItem>
            <NavLink href="#" active={active} onClick={handleOnClick(id)}>
                {label}
            </NavLink>
        </NavItem>
    )

    return (
        <Nav className={classNames('n2o-pill-filter', className)} pills {...rest}>
            {map(data, renderPills)}
        </Nav>
    )
}

Pills.propTypes = {
    data: PropTypes.array,
    onClick: PropTypes.func,
    className: PropTypes.string,
}
Pills.defaultProps = {
    // eslint-disable-next-line react/default-props-match-prop-types
    multiSelect: [],
}

export default Pills
