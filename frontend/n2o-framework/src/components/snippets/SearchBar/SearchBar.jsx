import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { batchActions } from 'redux-batched-actions'
import isString from 'lodash/isString'
import isEmpty from 'lodash/isEmpty'
import Button from 'reactstrap/lib/Button'
import {
    compose,
    withState,
    withHandlers,
    lifecycle,
    defaultProps,
} from 'recompose'
import onClickOutsideHOC from 'react-onclickoutside'

import InputText from '../../controls/InputText/InputText'

import { SearchBarPopUp } from './SearchBarPopUp'
import { SearchBarEmptyMenu } from './SearchBarEmptyMenu'

let timeoutId = null
const ENTER_KEY_CODE = 13

const SearchTrigger = {
    ENTER: 'ENTER',
    CHANGE: 'CHANGE',
    BUTTON: 'BUTTON',
}

function SearchBar({
    className,
    innerValue,
    icon,
    button,
    onClick,
    onBlur,
    onChange,
    onKeyDown,
    onFocus,
    placeholder,
    iconClear,
    onClear,
    menu,
    dropdownOpen,
    toggleDropdown,
    directionIconsInPopUp,
    descriptionFieldId,
    iconFieldId,
    labelFieldId,
    urlFieldId,
    onItemClick,
}) {
    const isIconClear = iconClear && innerValue

    SearchBar.handleClickOutside = () => toggleDropdown('false')

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
                        onFocus={() => batchActions([toggleDropdown('true'), onFocus && onFocus()])
                        }
                    />
                    {isIconClear && (
                        <i
                            className="n2o-search-bar__clear-icon fa fa-times"
                            onClick={onClear}
                        />
                    )}
                    {isString(icon) ? <i className={icon} /> : icon}
                </div>
                {isEmpty(menu) ? (
                    <SearchBarEmptyMenu
                        urlFieldId={urlFieldId}
                        dropdownOpen={dropdownOpen === 'true'}
                    />
                ) : (
                    <SearchBarPopUp
                        menu={menu}
                        dropdownOpen={dropdownOpen === 'true'}
                        directionIconsInPopUp={directionIconsInPopUp}
                        descriptionFieldId={descriptionFieldId}
                        iconFieldId={iconFieldId}
                        labelFieldId={labelFieldId}
                        urlFieldId={urlFieldId}
                        onItemClick={onItemClick}
                    />
                )}
            </div>
            {!!button && (
                <Button {...button} onClick={onClick}>
                    {button.label}
                    {button.icon && <i className={classNames('ml-2', button.icon)} />}
                </Button>
            )}
        </div>
    )
}

SearchBar.propTypes = {
    iconClear: PropTypes.bool,
    innerValue: PropTypes.string,
    onClick: PropTypes.func,
    toggleDropdown: PropTypes.func,
    onClear: PropTypes.func,
    onChange: PropTypes.func,
    onBlur: PropTypes.func,
    onKeyDown: PropTypes.func,
    onFocus: PropTypes.func,
    onItemClick: PropTypes.func,
    urlFieldId: PropTypes.string,
    dropdownOpen: PropTypes.string,
    iconFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    descriptionFieldId: PropTypes.string,
    /**
     * Класс компонента
     */
    className: PropTypes.string,
    /**
     * Начальное состояние строки поиска
     */
    initialValue: PropTypes.string,
    /**
     * Значение компонента
     */
    value: PropTypes.string,
    /**
     * Placeholder контрола
     */
    placeholder: PropTypes.string,
    /**
     * Триггер запуска колбека поиска
     */
    trigger: PropTypes.oneOf([
        SearchTrigger.CHANGE,
        SearchTrigger.ENTER,
        SearchTrigger.BUTTON,
    ]),
    /**
     * Настройка кнопки
     */
    button: PropTypes.object,
    /**
     * Иконка
     */
    icon: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.node,
        PropTypes.element,
        PropTypes.string,
    ]),
    /**
     * Коллбек поиска
     */
    onSearch: PropTypes.func,
    /**
     * Delay поиска при change триггере
     */
    throttleDelay: PropTypes.number,
    /**
     * данные и резолв для popUp
     */
    menu: PropTypes.array,
    /**
     * направление иконок и items в popUp: left(default), right
     */
    directionIconsInPopUp: PropTypes.string,
}

SearchBar.defaultProps = {
    trigger: SearchTrigger.CHANGE,
    button: false,
    icon: 'fa fa-search',
    directionIconsInPopUp: 'left',
    iconClear: true,
    onSearch: () => {},
}

const clickOutsideConfig = {
    handleClickOutside: () => SearchBar.handleClickOutside,
}

const enhance = compose(
    defaultProps({
        trigger: SearchTrigger.CHANGE,
        throttleDelay: 400,
    }),
    withState(
        'innerValue',
        'setInnerValue',
        ({ value, initialValue }) => initialValue || value,
    ),
    withState('dropdownOpen', 'toggleDropdown', 'false'),
    withHandlers({
        onClick: ({ innerValue, onSearch }) => () => onSearch(innerValue),
        onKeyDown: ({ innerValue, trigger, onSearch }) => ({ keyCode }) => {
            if (trigger === SearchTrigger.ENTER && keyCode === ENTER_KEY_CODE) {
                onSearch(innerValue)
            }
        },
        onChange: ({
            setInnerValue,
            trigger,
            throttleDelay,
            onSearch,
        }) => (value) => {
            setInnerValue(value)

            if (!value) {
                setInnerValue(null)
                onSearch(null)
            }

            if (trigger === SearchTrigger.CHANGE) {
                clearTimeout(timeoutId)
                timeoutId = setTimeout(() => onSearch(value), throttleDelay)
            }
        },
        onBlur: ({ setInnerValue, onSearch }) => (value) => {
            if (!value) {
                setInnerValue(null)
                onSearch(null)
            }
        },
        onClear: ({ setInnerValue, onSearch }) => () => {
            setInnerValue(null)
            onSearch(null)
        },
        onItemClick: ({ toggleDropdown }) => () => {
            toggleDropdown(false)
        },
    }),
    lifecycle({
        componentDidMount() {
            const { initSearchValue, setInnerValue, onSearch } = this.props

            if (initSearchValue !== undefined) {
                setInnerValue(initSearchValue)
                onSearch('')
            }
        },
        componentDidUpdate(prevProps) {
            const { value, setInnerValue } = this.props

            if (prevProps.value !== value) {
                setInnerValue(value)
            }
        },
    }),
)

export { SearchBar }
export default onClickOutsideHOC(enhance(SearchBar), clickOutsideConfig)
