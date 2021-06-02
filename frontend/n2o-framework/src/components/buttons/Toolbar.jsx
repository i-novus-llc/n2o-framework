import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import isUndefined from 'lodash/isUndefined'
import set from 'lodash/set'
import get from 'lodash/get'
import unset from 'lodash/unset'
import ButtonToolbar from 'reactstrap/lib/ButtonToolbar'
import ButtonGroup from 'reactstrap/lib/ButtonGroup'
import classNames from 'classnames'

import Factory from '../../core/factory/Factory'
import { BUTTONS } from '../../core/factory/factoryLevels'

function Toolbar({ className, toolbar, entityKey, onClick }) {
    const handleClick = (e) => {
        e.stopPropagation()
        onClick()
    }

    const remapButtons = (obj) => {
        const subMenu = get(obj, 'subMenu')
        const enabled = get(obj, 'enabled')

        if (!isUndefined(enabled)) {
            set(obj, 'disabled', !enabled)
            set(obj, 'entityKey', entityKey)

            unset(obj, 'enabled')
        }
        if (!isUndefined(subMenu)) {
            map(subMenu, item => remapButtons(item))
        }

        return obj
    }

    const renderButtons = props => <Factory level={BUTTONS} {...remapButtons(props)} entityKey={entityKey} />

    // eslint-disable-next-line react/prop-types
    const renderBtnGroup = ({ buttons }) => (
        <ButtonGroup>{map(buttons, renderButtons)}</ButtonGroup>
    )

    return (
        <ButtonToolbar
            className={classNames('buttons-toolbar', className)}
            onClick={handleClick}
        >
            {map(toolbar, renderBtnGroup)}
        </ButtonToolbar>
    )
}

Toolbar.propTypes = {
    toolbar: PropTypes.array,
    entityKey: PropTypes.string,
    onClick: PropTypes.func,
    className: PropTypes.string,
}

Toolbar.defaultProps = {
    toolbar: [],
    onClick: () => {},
}

export default Toolbar
