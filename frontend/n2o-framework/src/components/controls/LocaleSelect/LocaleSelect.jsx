import React from 'react'
import PropTypes from 'prop-types'
import { compose, withState, withHandlers } from 'recompose'
import map from 'lodash/map'
import classNames from 'classnames'
import Dropdown from 'reactstrap/lib/Dropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'

import localeSelectContainer from './LocaleSelectContainer'

export function LocaleSelect({
    className,
    value,
    onChange,
    locales,
    direction,
    opened,
    toggle,
}) {
    return (
        <Dropdown
            className={classNames('locale-select', className)}
            isOpen={opened}
            toggle={toggle}
            direction={direction}
        >
            <DropdownToggle>{value}</DropdownToggle>
            <DropdownMenu>
                {map(locales, (locale, index) => (
                    <DropdownItem onClick={() => onChange(locale)} key={index}>
                        {locale}
                    </DropdownItem>
                ))}
            </DropdownMenu>
        </Dropdown>
    )
}

LocaleSelect.propTypes = {
    className: PropTypes.string,
    value: PropTypes.string,
    onChange: PropTypes.func,
    locales: PropTypes.array,
    direction: PropTypes.oneOf(['top', 'right', 'bottom', 'left']),
    opened: PropTypes.bool,
    toggle: PropTypes.func,
}

LocaleSelect.defaultProps = {
    direction: 'left',
}

export default compose(
    withState('opened', 'setOpened', false),
    localeSelectContainer,
    withHandlers({
        toggle: ({ opened, setOpened }) => () => setOpened(!opened),
        onChange: ({ changeLocale }) => locale => changeLocale(locale),
    }),
)(LocaleSelect)
