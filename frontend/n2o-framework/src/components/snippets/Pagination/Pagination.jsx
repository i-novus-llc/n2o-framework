import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'

import { PaginationButton } from './PaginationButton'

const getLabel = (direction, icon, label) => {
    if (icon && label) {
        if (direction === 'left') {
            return (
                <>
                    <span className={icon} aria-hidden="true" />
                    {' '}
                    <span>{label}</span>
                </>
            )
        }

        if (direction === 'right') {
            return (
                <>
                    <span>{label}</span>
                    {' '}
                    <span className={icon} aria-hidden="true" />
                </>
            )
        }
    }

    if (icon) {
        return (<i className={icon} aria-hidden="true" />)
    }

    return label
}

const getControlButton = (buttonType, icon, label, activePage, onSelect, lastPage) => {
    const buttonsOptions = {
        first: { direction: 'left', props: { eventKey: 1, disabled: activePage === 1 } },
        last: { direction: 'right', props: { eventKey: lastPage, disabled: lastPage === activePage } },
        next: { direction: 'right', props: { eventKey: activePage + 1, disabled: lastPage === activePage } },
        prev: { direction: 'left', props: { eventKey: activePage - 1, disabled: activePage === 1 } },
    }

    const buttonOption = buttonsOptions[buttonType]

    return (
        <PaginationButton
            {...buttonOption.props}
            label={getLabel(buttonOption.direction, icon, label)}
            onSelect={onSelect}
            tabIndex={0}
        />
    )
}

/**
 * Рендер тела компонента. Алгоритм автоматически высчитывает страницы до и после текущей
 * @param activePage {number} - номер активной страницы
 * @param maxPages {number} - максимальное кол-во отображаемых кнопок перехода между страницами
 * @param onSelect {function} - callback нажатия по кнопке страницы
 * @param totalPages {number} - общее количество страниц
 * @returns {Array} - вовзращает список кнопок
 */
const renderBodyPaging = (activePage, maxPages, onSelect, totalPages) => {
    const getPager = (totalPages, currentPage, maxPages) => {
        let startPage
        let endPage

        if (totalPages <= maxPages) {
            // if we have less than maxPages so show all
            startPage = 1
            endPage = totalPages
        } else {
            // we have more than maxPages so calculate start and end pages
            const middle = Math.ceil(maxPages / 2)

            if (currentPage <= middle) {
                startPage = 1
                endPage = maxPages
            } else if (currentPage + (maxPages - middle) >= totalPages) {
                startPage = totalPages - (maxPages - 1)
                endPage = totalPages
            } else {
                startPage = currentPage - Math.floor(maxPages / 2)
                endPage = currentPage + middle - 1
            }
        }

        return [...Array((endPage + 1) - startPage).keys()].map(i => startPage + i)
    }

    return getPager(totalPages, activePage, maxPages).map(page => (
        <PaginationButton
            key={page.toString()}
            tabIndex={0}
            eventKey={page}
            label={page}
            active={page === activePage}
            onSelect={onSelect}
        />
    ))
}

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {boolean} prev - показать/скрыть кнопку быстрого перехода на предыдущую страницу
 * @reactProps {boolean} prevIcon - Вид иконки быстрого перехода на предудущую страницу
 * @reactProps {boolean} prevLabel - текс кнопки
 * @reactProps {boolean} next - показать/скрыть кнопку быстрого перехода на следующую страницу
 * @reactProps {boolean} nextIcon - Вид иконки быстрого перехода на следующую страницу
 * @reactProps {boolean} nextLabel - текст кнопки
 * @reactProps {boolean} first - показать/скрыть кнопку быстрого перехода на первую страницу
 * @reactProps {boolean} firstIcon - Вид иконки быстрого перехода на первую страницу
 * @reactProps {boolean} firstLabel - текст кнопки
 * @reactProps {boolean} last - показать/скрыть кнопку быстрого перехода на последнюю страницу
 * @reactProps {boolean} lastIcon - Вид иконки быстрого перехода на последнюю страницу
 * @reactProps {boolean} lastLabel - текст кнопки
 * @reactProps {boolean} showCount - показать индикатор общего кол-ва записей
 * @reactProps {boolean} showSinglePage - показывать компонент, если страница единственная
 * @reactProps {number} maxPages - максимальное кол-во отображаемых кнопок перехода между страницами
 * @reactProps {number} count - общее кол-во записей
 * @reactProps {number} size - кол-во записей на одной странице
 * @reactProps {number} activePage - номер активной страницы
 * @reactProps {function} onSelect - callback нажатия по кнопке страницы
 * @reactProps {string} className - класс для списка внутри nav
 * @example
 * <Pagination onSelect={this.changePage}
 *             activePage={datasource.page}
 *             count={datasource.count}
 *             size={datasource.size}
 *             maxPages={5}/>
 */
const Pagination = (props) => {
    const {
        layout = 'separated',
        activePage = 1,
        count = 1,
        size = 1,
        maxPages = 5,
        first = false,
        last = false,
        prev = false,
        next = false,
        showCount = true,
        showSinglePage = false,
        onSelect,
        className,
        prevIcon = 'fa fa-angle-left',
        prevLabel = null,
        nextIcon = 'fa fa-angle-right',
        nextLabel = null,
        firstIcon = 'fa fa-angle-double-left',
        firstLabel = null,
        lastIcon = 'fa fa-angle-double-right',
        lastLabel = null,
        style = { display: 'flex', alignItems: 'baseline' },
        t = () => {},
    } = props

    const pages = Math.ceil(count / size) || 1
    const lastPage = Math.ceil(count / size)

    return (
        <nav
            className={classNames('n2o-pagination', className)}
            style={style}
        >
            {showSinglePage || pages > 1 ? (
                <ul className={classNames('pagination', 'd-inline-flex', layout)}>
                    {first && getControlButton('first', firstIcon, firstLabel, activePage, onSelect, lastPage)}
                    {prev && getControlButton('prev', prevIcon, prevLabel, activePage, onSelect, lastPage)}
                    {renderBodyPaging(activePage, maxPages, onSelect, pages)}
                    {next && getControlButton('next', nextIcon, nextLabel, activePage, onSelect, lastPage)}
                    {last && getControlButton('last', lastIcon, lastLabel, activePage, onSelect, lastPage)}
                </ul>
            ) : null}

            {showCount && (
                <span
                    className="n2o-pagination-info"
                    style={{
                        paddingLeft: showSinglePage || pages > 1 ? '1rem' : 0,
                        display: 'inline-flex',
                    }}
                >
                    {`${t('paginationTotal')} ${count}`}
                    <>&nbsp;</>
                    {t('paginationInterval', { postProcess: 'interval', count })}
                </span>
            )}
        </nav>
    )
}

Pagination.propTypes = {
    /**
     *  Стиль
     * */
    layout: PropTypes.oneOf([
        'bordered',
        'flat',
        'separated',
        'flat-rounded',
        'bordered-rounded',
        'separated-rounded',
    ]),
    /**
     * Показать/скрыть кнопку быстрого перехода на предыдущую страницу
     */
    prev: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на предудущую страницу
     * */
    prevIcon: PropTypes.string,
    /**
     * Текст кнопки 'Назад'
     */
    prevLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на следующую страницу
     */
    next: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на следующую страницу
     */
    nextIcon: PropTypes.string,
    /**
     * Текст кнопки 'Вперед'
     */
    nextLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на первую страницу
     */
    first: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на первую страницу
     * */
    firstIcon: PropTypes.string,
    /**
     * Текст кнопки 'First'
     */
    firstLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на последнюю страницу
     */
    last: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на последнюю страницу
     * */
    lastIcon: PropTypes.string,
    /**
     * Текст кнопки 'Last'
     */
    lastLabel: PropTypes.string,
    /**
     * Показать индикатор общего кол-ва записей
     */
    showCount: PropTypes.bool,
    /**
     * Скрывать компонент, если страница единственная
     */
    showSinglePage: PropTypes.bool,
    /**
     * Максимальное кол-во кнопок перехода между страницами
     */
    maxPages: PropTypes.number,
    /**
     * Общее кол-во записей
     */
    count: PropTypes.number,
    /**
     * Кол-во записей на одной странице
     */
    size: PropTypes.number,
    /**
     * Номер активной страницы
     */
    activePage: PropTypes.number,
    /**
     * Сallback нажатия по кнопке страницы
     */
    onSelect: PropTypes.func,
    /**
     * Класс для списка внутри nav
     */
    className: PropTypes.string,
    t: PropTypes.func,
    style: PropTypes.object,
}

export default withTranslation()(Pagination)
