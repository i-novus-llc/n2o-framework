import React, { useState } from 'react'
import classNames from 'classnames'
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { EMPTY_ARRAY } from '../../../utils/emptyTypes'

import { LocaleSelectContainer, type Props } from './LocaleSelectContainer'

function LocaleSelectBody({
    className,
    value,
    locales = EMPTY_ARRAY,
    changeLocale,
    direction = 'left',
}: Props) {
    const [opened, setOpened] = useState(false)
    const toggle = () => setOpened(!opened)

    return (
        <Dropdown
            className={classNames('locale-select', className)}
            isOpen={opened}
            toggle={toggle}
            direction={direction}
        >
            <DropdownToggle>{value}</DropdownToggle>
            <DropdownMenu>
                {locales.map(locale => (
                    <DropdownItem onClick={() => changeLocale(locale)} key={locale}>{locale}</DropdownItem>
                ))}
            </DropdownMenu>
        </Dropdown>
    )
}

export const LocaleSelect = LocaleSelectContainer(LocaleSelectBody)

export default LocaleSelect
