import React, { useCallback, useEffect, useLayoutEffect, useState } from 'react'
import classNames from 'classnames'
import isString from 'lodash/isString'
import isEmpty from 'lodash/isEmpty'
import set from 'lodash/set'
import { Button } from 'reactstrap'
import onClickOutsideHOC from 'react-onclickoutside'
import { InputText } from '@i-novus/n2o-components/lib/inputs/InputText'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { SearchBarPopUp } from './SearchBarPopUp'
import { SearchBarEmptyMenu } from './SearchBarEmptyMenu'
import { SEARCH_TRIGGER, type SearchBarProps } from './types'

let timeoutId: NodeJS.Timeout | null = null
const ENTER_KEY_CODE = 13

function SearchBar({
    className,
    onFocus,
    placeholder,
    menu,
    descriptionFieldId,
    iconFieldId,
    labelFieldId,
    urlFieldId,
    initialValue,
    value,
    initSearchValue,
    onSearch = () => {},
    trigger = SEARCH_TRIGGER.CHANGE,
    throttleDelay = 400,
    icon = 'fa fa-search',
    button = null,
    iconClear = true,
    directionIconsInPopUp = 'left',
}: SearchBarProps) {
    const [dropdownOpen, toggleDropdown] = useState(false)
    const [innerValue, setInnerValue] = useState<string | null | undefined>(initialValue || value)
    const isIconClear = iconClear && innerValue

    const onClick = useCallback(() => { onSearch(innerValue) }, [innerValue, onSearch])

    const onKeyDown = useCallback(({ keyCode }) => {
        if (trigger === SEARCH_TRIGGER.ENTER && keyCode === ENTER_KEY_CODE) {
            onSearch(innerValue)
        }
    }, [innerValue, onSearch, trigger])

    const onChange = useCallback((value) => {
        setInnerValue(value)

        if (!value) {
            setInnerValue(null)
            onSearch(null)
        }

        if (trigger === SEARCH_TRIGGER.CHANGE) {
            clearTimeout(timeoutId as NodeJS.Timeout)
            timeoutId = setTimeout(() => onSearch(value), throttleDelay)
        }
    }, [onSearch, throttleDelay, trigger])

    const onBlur = useCallback(() => {
        if (!value) {
            setInnerValue(null)
            onSearch(null)
        }
    }, [onSearch, value])

    const onClear = useCallback(() => {
        setInnerValue(null)
        onSearch(null)
    }, [onSearch])

    const onItemClick = () => toggleDropdown(false)

    useLayoutEffect(() => {
        // TODO handleClickOutside присваивается для работы библиотеки react-onclickoutside,
        //  выглядит странно, нужно пересмотреть
        set(SearchBar, 'handleClickOutside', () => toggleDropdown(false))
    }, [])

    useEffect(() => {
        if (initSearchValue !== undefined) {
            setInnerValue(initSearchValue)
            onSearch('')
        }
    }, [])

    useEffect(() => {
        setInnerValue(value)
    }, [value])

    return (
        <div className={classNames('n2o-search-bar', className)}>
            <div className="n2o-search-bar__control">
                <div>
                    <InputText
                        onKeyDown={onKeyDown}
                        value={innerValue}
                        onChange={onChange}
                        placeholder={placeholder}
                        onBlur={onBlur}
                        onFocus={() => {
                            toggleDropdown(true)
                            if (onFocus) { onFocus() }
                        }}
                    />
                    {isIconClear && (
                        <Icon
                            className={classNames(
                                'n2o-search-bar__clear-icon fa fa-times',
                                { 'with-search': icon },
                            )}
                            onClick={onClear}
                        />
                    )}
                    {isString(icon) ? <Icon className={icon} /> : icon}
                </div>
                {isEmpty(menu) ? (
                    <SearchBarEmptyMenu
                        urlFieldId={urlFieldId}
                        dropdownOpen={dropdownOpen}
                    />
                ) : (
                    <SearchBarPopUp
                        menu={menu}
                        dropdownOpen={dropdownOpen}
                        directionIconsInPopUp={directionIconsInPopUp}
                        descriptionFieldId={descriptionFieldId}
                        iconFieldId={iconFieldId}
                        labelFieldId={labelFieldId}
                        urlFieldId={urlFieldId}
                        onItemClick={onItemClick}
                    />
                )}
            </div>
            {button && (
                <Button {...button} onClick={onClick}>
                    {button.label}
                    {button.icon && <i className={classNames('ml-2', button.icon)} />}
                </Button>
            )}
        </div>
    )
}

// @ts-ignore конфиг для react-onclickoutside, это работает, но нормально не типизируется.
// расчет на использовании классовых компонентов
const clickOutsideConfig = { handleClickOutside: () => SearchBar.handleClickOutside }

export { SearchBar }
export default onClickOutsideHOC(SearchBar, clickOutsideConfig)
