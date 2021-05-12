import React, { Component } from 'react'
import classNames from 'classnames'
import ReactDom from 'react-dom'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import PropTypes from 'prop-types'
// eslint-disable-next-line import/no-unresolved,import/no-extraneous-dependencies
import { authProvider } from 'n2o-auth'
import {
    WindowScroller,
    AutoSizer,
    CellMeasurer,
    CellMeasurerCache,
    List as Virtualizer,
} from 'react-virtualized'

import { getIndex } from '../Table/Table'
import { SECURITY_CHECK } from '../../../core/auth/authTypes'

import ListItem from './ListItem'
import ListMoreButton from './ListMoreButton'

const SCROLL_OFFSET = 100

/**
 * Компонент List
 * @reactProps {number|string} selectedId - id выбранной записи
 * @reactProps {boolean} autoFocus - фокус при инициализации на перой или на selectedId строке
 * @reactProps {function} onItemClick - callback при клике на строку
 * @reactProps {object} rowClick - кастомное действие клика
 * @reactProps {function} onFetchMore - callback при клика на "Загрузить еще" или скролле
 * @reactProps {string|number} selectedId - id выбранной записи
 */
class List extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedIndex: props.hasSelect
                ? getIndex(props.data, props.selectedId)
                : null,
            data: props.data,
            permissions: null,
        }
        this.cache = new CellMeasurerCache({
            fixedWidth: true,
            defaultHeight: 90,
        })

        this.scrollTimeoutId = null

        this.renderRow = this.renderRow.bind(this)
        this.onItemClick = this.onItemClick.bind(this)
        this.fetchMore = this.fetchMore.bind(this)
        this.onScroll = this.onScroll.bind(this)
        this.setListContainerRef = this.setListContainerRef.bind(this)
        this.setVirtualizerRef = this.setVirtualizerRef.bind(this)
        this.setWindowScrollerRef = this.setWindowScrollerRef.bind(this)
    }

    componentDidMount() {
        const { fetchOnScroll, authProvider, rows } = this.props

        if (fetchOnScroll) {
            this.listContainer.addEventListener('scroll', this.onScroll, true)
        }

        if (!isEmpty(rows)) {
            authProvider(SECURITY_CHECK, {
                config: rows.security,
            }).then((permissions) => {
                this.setState({ permissions })
            })
        }
    }

    componentDidUpdate(prevProps) {
        const {
            data,
            hasMoreButton,
            fetchOnScroll,
            maxHeight,
            selectedId,
            rows,
            authProvider,
        } = this.props

        if (!isEqual(prevProps, this.props)) {
            let state = {}

            if (hasMoreButton && !fetchOnScroll && !isEqual(prevProps.data, data)) {
                if (maxHeight) {
                    this.virtualizer.scrollToRow(data.length)
                } else {
                    // eslint-disable-next-line react/no-find-dom-node
                    const virtualizer = ReactDom.findDOMNode(this.virtualizer)

                    if (virtualizer) {
                        window.scrollTo(0, virtualizer.scrollHeight)
                    }
                }
            }

            if (!isEqual(prevProps.data, data)) {
                state = {
                    ...state,
                    data: hasMoreButton ? [...data, {}] : data,
                }
            }

            if (selectedId && !isEqual(prevProps.selectedId, selectedId)) {
                state = {
                    ...state,
                    selectedIndex: getIndex(data, selectedId),
                }
            }

            this.setState(state, () => {
                if (this.virtualizer) {
                    // noinspection JSUnresolvedFunction
                    this.virtualizer.forceUpdateGrid()
                }

                this.resizeAll()
            })
        }

        if (!isEqual(rows, prevProps.rows)) {
            authProvider(SECURITY_CHECK, {
                config: rows.security,
            }).then((permissions) => {
                this.setState({ permissions })
            })
        }
    }

    componentWillUnmount() {
        const { fetchOnScroll } = this.props

        if (fetchOnScroll) {
            this.listContainer.removeEventListener('scroll', this.onScroll)
        }
    }

    setListContainerRef(el) {
        this.listContainer = el
    }

    setWindowScrollerRef(el) {
        this.windowScroller = el
    }

    setVirtualizerRef(el) {
        this.virtualizer = el
    }

    onItemClick(index, runCallback = true) {
        const { onItemClick, rowClick, hasSelect } = this.props

        if (!rowClick && hasSelect) {
            this.setState({ selectedIndex: index }, () => {
                if (this.virtualizer) {
                    // noinspection JSUnresolvedFunction
                    this.virtualizer.forceUpdateGrid()
                }
            })
        }

        if (runCallback) { onItemClick(index) }
    }

    fetchMore() {
        const { onFetchMore } = this.props

        onFetchMore()
    }

    onScroll(event) {
        clearTimeout(this.scrollTimeoutId)

        this.scrollTimeoutId = setTimeout(() => {
            const scrollPosition = event.target.scrollTop + event.target.clientHeight
            const minScrollToLoad = event.target.scrollHeight - SCROLL_OFFSET

            if (
                scrollPosition >= minScrollToLoad ||
        scrollPosition === event.target.scrollHeight
            ) {
                this.fetchMore()
            }
        }, 300)
    }

  resizeAll = () => {
      this.cache.clearAll()
      if (this.virtualizer) {
          // noinspection JSUnresolvedFunction
          this.virtualizer.recomputeRowHeights()
      }
  };

  renderRow({ index, key, style, parent }) {
      const {
          divider,
          hasMoreButton,
          fetchOnScroll,
          hasSelect,
          rows,
      } = this.props
      const { data, permissions, selectedIndex } = this.state
      const moreBtn = null

      if (index === data.length - 1 && hasMoreButton && !fetchOnScroll) {
          return (
              <CellMeasurer
                  key={key}
                  cache={this.cache}
                  parent={parent}
                  columnIndex={0}
                  rowIndex={index}
              >
                  <ListMoreButton style={style} onClick={this.fetchMore} />
              </CellMeasurer>
          )
      }

      return (
          <>
              <CellMeasurer
                  key={key}
                  cache={this.cache}
                  parent={parent}
                  columnIndex={0}
                  rowIndex={index}
              >
                  <ListItem
                      {...data[index]}
                      hasSelect={hasSelect}
                      key={key}
                      style={style}
                      divider={divider}
                      selected={selectedIndex === index}
                      onClick={() => this.onItemClick(index, isEmpty(rows) || permissions)
                      }
                  />
              </CellMeasurer>
              {moreBtn}
          </>
      )
  }

  render() {
      const { className, maxHeight, t } = this.props
      const { data } = this.state

      return (
          <div
              ref={this.setListContainerRef}
              className={classNames('n2o-widget-list', className)}
          >
              {(!data || isEmpty(data)) && (
                  <div className="n2o-widget-list--empty-view text-muted">
                      {t('noData')}
                  </div>
              )}
              {data && !isEmpty(data) && (
                  <div className="n2o-widget-list-container">
                      {maxHeight ? (
                          <AutoSizer style={{ height: '100%' }}>
                              {({ width }) => (
                                  <Virtualizer
                                      ref={this.setVirtualizerRef}
                                      width={width}
                                      height={maxHeight}
                                      deferredMeasurementCache={this.cache}
                                      rowHeight={this.cache.rowHeight}
                                      rowRenderer={this.renderRow}
                                      rowCount={data.length}
                                      overscanRowCount={5}
                                  />
                              )}
                          </AutoSizer>
                      ) : (
                          <WindowScroller
                              ref={this.setWindowScrollerRef}
                              scrollElement={window}
                          >
                              {({
                                  height,
                                  isScrolling,
                                  onChildScroll,
                                  scrollTop,
                              }) => (
                                  <AutoSizer style={{ height: '100%' }}>
                                      {({ width }) => (
                                          <Virtualizer
                                              ref={this.setVirtualizerRef}
                                              autoHeight
                                              height={height}
                                              isScrolling={isScrolling}
                                              onScroll={onChildScroll}
                                              overscanRowCount={5}
                                              rowCount={data.length}
                                              rowHeight={this.cache.rowHeight}
                                              rowRenderer={this.renderRow}
                                              scrollTop={scrollTop}
                                              width={width}
                                          />
                                      )}
                                  </AutoSizer>
                              )}
                          </WindowScroller>
                      )}
                  </div>
              )}
          </div>
      )
  }
}

List.propTypes = {
    /**
   * Callback на клик по строке
   */
    onItemClick: PropTypes.func,
    /**
   * Флаг включения выбора строк
   */
    hasSelect: PropTypes.bool,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Данные
   */
    data: PropTypes.arrayOf(PropTypes.object),
    /**
   * Экшен клика по строке
   */
    rowClick: PropTypes.object,
    /**
   * Флаг включения кнопки "Загрузить еще"
   */
    hasMoreButton: PropTypes.bool,
    /**
   * Callback на "Загрузить еще"
   */
    onFetchMore: PropTypes.func,
    /**
   * Максимальная высота
   */
    maxHeight: PropTypes.number,
    /**
   * Флаг получения данных при скролле
   */
    fetchOnScroll: PropTypes.bool,
    /**
   * Линия разделитель
   */
    divider: PropTypes.bool,
    /**
   * Настройка security
   */
    rows: PropTypes.object,
    selectedId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Функция проверки security
   */
    authProvider: PropTypes.func,
    t: PropTypes.func,
}
List.defaultProps = {
    onItemClick: () => {},
    onFetchMore: () => {},
    t: () => {},
    hasSelect: false,
    data: [],
    rowClick: false,
    hasMoreButton: false,
    fetchOnScroll: false,
    divider: true,
    rows: {},
    authProvider,
}

export { List }

export default List
