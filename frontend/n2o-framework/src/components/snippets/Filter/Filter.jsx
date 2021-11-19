import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import Buttons from './Buttons'

/**
 * Компонент фильтр (Filter)
 * @reactProps {node} children - элемент управления
 * @reactProps {object} style - кастомные стили
 * @reactProps {function} onSearch - callback на поиск
 * @reactProps {function} onReset - callback на сброс
 * @reactProps {string} className - класс корневого элемента
 * @reactProps {string} searchLabel - лейбел кнопки поиска
 * @reactProps {string} resetLabel - лейбел кнопки сброса
 * @reactProps {boolean} visible - флаг видимости
 * @example
 * <Link to="/path/1" onClick={this.changeUrl}>Ссылка</Link>
 */
export class Filter extends React.Component {
    constructor(props) {
        super(props)
        this.onReset = this.onReset.bind(this)
        this.onSearch = this.onSearch.bind(this)
    }

    onReset() {
        const { onReset } = this.props

        onReset()
    }

    onSearch() {
        const { onSearch } = this.props

        onSearch()
    }

    render() {
        const {
            className,
            style,
            visible,
            resetLabel,
            searchLabel,
            hideButtons,
            children,
        } = this.props

        return visible ? (
            <div className={classNames('n2o-filter', className)} style={style}>
                {children}
                {!hideButtons ? (
                    <Buttons
                        searchLabel={searchLabel}
                        resetLabel={resetLabel}
                        onSearch={this.onSearch}
                        onReset={this.onReset}
                    />
                ) : null}
            </div>
        ) : null
    }
}

Filter.propTypes = {
    children: PropTypes.node,
    style: PropTypes.object,
    /**
     * Callback на поиск
     */
    onSearch: PropTypes.func,
    /**
     * Callback на сброс
     */
    onReset: PropTypes.func,
    className: PropTypes.string,
    /**
     * Текст кнопки поиска
     */
    searchLabel: PropTypes.string,
    /**
     * Текст кнопки сброса
     */
    resetLabel: PropTypes.string,
    /**
     * Видимость
     */
    visible: PropTypes.bool,
    /**
     * Флаг скрытия кнопок
     */
    hideButtons: PropTypes.bool,
}

Filter.defaultProps = {
    onSearch: () => {},
    onReset: () => {},
    visible: true,
    style: {},
    className: '',
    hideButtons: false,
}
