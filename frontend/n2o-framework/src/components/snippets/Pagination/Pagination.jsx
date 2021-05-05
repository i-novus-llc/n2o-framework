import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'
import { withTranslation } from 'react-i18next'

import PaginationButton from './PaginationButton'

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {boolean} prev - показать/скрыть кнопку быстрого перехода на предыдущую страницу
 * @reactProps {boolean} prevText - текс кнопки
 * @reactProps {boolean} next - показать/скрыть кнопку быстрого перехода на следующую страницу
 * @reactProps {boolean} nextText - текст кнопки
 * @reactProps {boolean} first - показать/скрыть кнопку быстрого перехода на первую страницу
 * @reactProps {boolean} last - показать/скрыть кнопку быстрого перехода на последнюю страницу
 * @reactProps {boolean} lazy - активировать режим "ленивой" пейджинации
 * @reactProps {boolean} withoutBody - скрыть тело пагинации
 * @reactProps {boolean} showCountRecords - показать индикатор общего кол-ва записей
 * @reactProps {boolean} hideSinglePage - скрывать компонент, если страница единственная
 * @reactProps {number} maxButtons - максимальное кол-во кнопок перехода между страницами
 * @reactProps {number} stepIncrement - шаг дополнительной кнопки (1,2.3 ... 11)
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
 *             maxButtons={4}
 *             stepIncrement={10} />
 */
class Pagination extends React.Component {
    /**
   * Рендер тела компонента. Алгоритм автоматически высчитывает страницы до и после текущей
   * @param activePage
   * @param pages
   * @param maxButtons
   * @param stepIncrement
   * @param onSelect
   * @returns {Array} - вовзращает список кнопок
   */
    renderBodyPaging(activePage, pages, maxButtons, stepIncrement, onSelect) {
        const pageButtons = []

        let startPage
        let endPage

        if (maxButtons && maxButtons < pages) {
            startPage = Math.max(
                Math.min(
                    activePage - Math.floor(maxButtons / 2, 10),
                    pages - maxButtons + 1,
                ),
                1,
            )
            endPage = startPage + maxButtons
        } else {
            startPage = 1
            endPage = maxButtons
        }

        if (endPage > pages) {
            endPage = pages
        }

        for (let page = startPage; page <= endPage; ++page) {
            pageButtons.push(
                <PaginationButton
                    key={page}
                    tabIndex={0}
                    eventKey={page}
                    label={page}
                    active={page === activePage}
                    onSelect={onSelect}
                />,
            )
        }

        if (stepIncrement && endPage < pages - 1) {
            pageButtons.push(
                <PaginationButton
                    label="..."
                    tabIndex={-1}
                    key="ellipsisMiddle"
                    noBorder
                    disabled
                />,
            )
            pageButtons.push(
                <PaginationButton
                    tabIndex={0}
                    key={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    eventKey={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    label={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    onSelect={onSelect}
                />,
            )
            activePage + stepIncrement < pages &&
        pageButtons.push(
            <PaginationButton
                label="..."
                tabIndex={-1}
                key="ellipsisLast"
                noBorder
                disabled
            />,
        )
        } else if (stepIncrement && endPage == pages - 1) {
            pageButtons.push(
                <PaginationButton
                    key={pages}
                    eventKey={pages}
                    label={pages}
                    onSelect={onSelect}
                />,
            )
        }

        if (startPage > 1) {
            if (startPage > 2) {
                pageButtons.unshift(
                    <PaginationButton
                        label="..."
                        key="ellipsisFirst"
                        tabIndex={-1}
                        noBorder
                        disabled
                    />,
                )
            }

            pageButtons.unshift(
                <PaginationButton
                    key={1}
                    eventKey={1}
                    label="1"
                    tabIndex={0}
                    onSelect={onSelect}
                />,
            )
        }

        return pageButtons
    }

    /**
   * Базовый рендер компонента
   */
    render() {
        const {
            activePage,
            count,
            size,
            maxButtons,
            stepIncrement,
            first,
            last,
            prev,
            next,
            showCountRecords,
            hideSinglePage,
            lazy,
            onSelect,
            className,
            withoutBody,
            prevText,
            nextText,
            t,
            ...props
        } = this.props
        const pages = Math.ceil(count / size, 10) || 1
        const lastPage = Math.ceil(count / size)

        return (
            <nav
                className="n2o-pagination"
                style={{ display: 'flex', alignItems: 'baseline' }}
            >
                {hideSinglePage && pages === 1 ? null : (
                    <ul className={cx('pagination', 'd-inline-flex', className)}>
                        {first && (
                            <PaginationButton
                                eventKey={1}
                                label="&laquo;"
                                disabled={activePage === 1}
                                onSelect={onSelect}
                                tabIndex={1}
                            />
                        )}
                        {prev && (
                            <PaginationButton
                                eventKey={activePage - 1}
                                label={prevText || '&lsaquo;'}
                                disabled={activePage === 1}
                                onSelect={onSelect}
                                tabIndex={0}
                            />
                        )}
                        {!withoutBody &&
              this.renderBodyPaging(
                  activePage,
                  pages,
                  maxButtons,
                  stepIncrement,
                  onSelect,
              )}
                        {next && (
                            <PaginationButton
                                eventKey={activePage + 1}
                                label={nextText || '&rsaquo;'}
                                disabled={activePage >= count}
                                onSelect={onSelect}
                                tabIndex={0}
                            />
                        )}
                        {last && (
                            <PaginationButton
                                eventKey={lastPage}
                                label="&raquo;"
                                disabled={lastPage === activePage}
                                onSelect={onSelect}
                                tabIndex={0}
                            />
                        )}
                    </ul>
                )}
                {showCountRecords && (
                    <span
                        className="n2o-pagination-info"
                        style={{
                            paddingLeft: hideSinglePage && pages === 1 ? 0 : '1rem',
                            display: 'inline-flex',
                        }}
                    >
                        {`${t('paginationTotal')} ${count}`}

            &nbsp;
                        {t('paginationInterval', { postProcess: 'interval', count })}
                    </span>
                )}
            </nav>
        )
    }
}

Pagination.propTypes = {
    /**
   * Показать/скрыть кнопку быстрого перехода на предыдущую страницу
   */
    prev: PropTypes.bool,
    /**
   * Текст кнопки 'Назад'
   */
    prevText: PropTypes.string,
    /**
   * Показать/скрыть кнопку быстрого перехода на следующую страницу
   */
    next: PropTypes.bool,
    /**
   * Текст кнопки 'Вперед'
   */
    nextText: PropTypes.string,
    /**
   * Показать/скрыть кнопку быстрого перехода на первую страницу
   */
    first: PropTypes.bool,
    /**
   * Показать/скрыть кнопку быстрого перехода на последнюю страницу
   */
    last: PropTypes.bool,
    /**
   * Активировать режим "ленивой" пейджинации
   */
    lazy: PropTypes.bool,
    /**
   * Скрыть тело пагинации
   */
    withoutBody: PropTypes.bool,
    /**
   * Показать индикатор общего кол-ва записей
   */
    showCountRecords: PropTypes.bool,
    /**
   * Скрывать компонент, если страница единственная
   */
    hideSinglePage: PropTypes.bool,
    /**
   * Максимальное кол-во кнопок перехода между страницами
   */
    maxButtons: PropTypes.number,
    /**
   * Шаг дополнительной кнопки (1,2.3 ... 11)
   */
    stepIncrement: PropTypes.number,
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
}

Pagination.defaultProps = {
    prev: false,
    prevText: null,
    next: false,
    nextText: null,
    first: false,
    last: false,
    lazy: false,
    withoutBody: false,
    showCountRecords: true,
    hideSinglePage: true,
    maxButtons: 4,
    count: 1,
    size: 1,
    activePage: 1,
    t: () => {},
}

export default withTranslation()(Pagination)
