import React, { Component, LegacyRef } from 'react'
import classNames from 'classnames'
import ReactDom from 'react-dom'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import {
    WindowScroller,
    AutoSizer,
    CellMeasurer,
    CellMeasurerCache,
    List as Virtualizer,
} from 'react-virtualized'
import { CellMeasurerProps } from 'react-virtualized/dist/es/CellMeasurer'
import { ListRowRenderer } from 'react-virtualized/dist/es/List'

import { getIndex } from '../Table/utils'
import { EMPTY_ARRAY, EMPTY_OBJECT, NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { ListItem } from './ListItem'
import { ListMoreButton } from './ListMoreButton'
import { ListProps } from './types'

const SCROLL_OFFSET = 100

export interface State {
    selectedIndex: number | null
    data: ListProps['data']
}

/* FIXME виджет требует рефакторинга frontend + backend xml api, избавиться от react-virtualized */

/**
 * Компонент List
 * @reactProps {boolean} autoFocus - фокус при инициализации на перой или на selectedId строке
 * @reactProps {function} onItemClick - callback при клике на строку
 * @reactProps {object} rowClick - кастомное действие клика
 * @reactProps {function} onFetchMore - callback при клика на "Загрузить еще" или скролле
 * @reactProps {string|number} selectedId - id выбранной записи
 */

class List extends Component<ListProps, State> {
    static defaultProps = {
        onItemClick: NOOP_FUNCTION,
        onFetchMore: NOOP_FUNCTION,
        t: NOOP_FUNCTION,
        hasSelect: false,
        data: EMPTY_ARRAY,
        rowClick: false,
        hasMoreButton: false,
        fetchOnScroll: false,
        divider: true,
        rows: EMPTY_OBJECT,
    }

    private readonly cache: CellMeasurerCache

    /* eslint-disable @typescript-eslint/no-explicit-any */
    private scrollTimeoutId: any

    private listContainer: any

    private virtualizer: any

    private windowScroller: any

    constructor(props: ListProps) {
        super(props)
        this.state = {
            selectedIndex: props.hasSelect ? getIndex(props.data, props.selectedId) : null,
            data: props.data,
        }
        this.cache = new CellMeasurerCache({
            fixedWidth: true,
            defaultHeight: 90,
            minHeight: 60,
        })

        this.scrollTimeoutId = null
    }

    componentDidMount() {
        const { fetchOnScroll } = this.props

        if (fetchOnScroll) {
            this.listContainer.addEventListener('scroll', this.onScroll, true)
        }
    }

    componentDidUpdate(prevProps: ListProps) {
        const {
            data,
            hasMoreButton,
            fetchOnScroll,
            maxHeight,
            selectedId,
        } = this.props

        if (!isEqual(prevProps, this.props)) {
            let state = {}

            if (hasMoreButton && !fetchOnScroll && !isEqual(prevProps.data, data)) {
                if (maxHeight) {
                    this.virtualizer.scrollToRow(data.length)
                } else {
                    // eslint-disable-next-line react/no-find-dom-node
                    const virtualizer = ReactDom.findDOMNode(this.virtualizer) as Element

                    if (virtualizer) {
                        window.scrollTo(0, virtualizer?.scrollHeight)
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
                    this.virtualizer.forceUpdateGrid()
                }

                this.resizeAll()
            })
        }
    }

    componentWillUnmount() {
        const { fetchOnScroll } = this.props

        if (fetchOnScroll) {
            this.listContainer.removeEventListener('scroll', this.onScroll)
        }
    }

    setListContainerRef = (el: LegacyRef<HTMLDivElement>) => {
        this.listContainer = el
    }

    setWindowScrollerRef = (el: LegacyRef<HTMLDivElement>) => {
        this.windowScroller = el
    }

    setVirtualizerRef = (el: LegacyRef<List>) => {
        this.virtualizer = el
    }

    onItemClick = (index: number, runCallback = true) => {
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

    fetchMore = () => {
        const { onFetchMore } = this.props

        onFetchMore()
    }

    onScroll = (event: React.UIEvent<HTMLElement>) => {
        clearTimeout(this.scrollTimeoutId)
        const target = event.target as HTMLElement

        this.scrollTimeoutId = setTimeout(() => {
            const scrollPosition = target.scrollTop + target.clientHeight
            const minScrollToLoad = target.scrollHeight - SCROLL_OFFSET

            if (scrollPosition >= minScrollToLoad || scrollPosition === target.scrollHeight) {
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
    }

    renderRow = ({ index, key, style, parent }: CellMeasurerProps) => {
        const {
            divider,
            hasMoreButton,
            fetchOnScroll,
            hasSelect,
            rows,
        } = this.props
        const { data, selectedIndex } = this.state
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
                    {
                        ({ measure }) => (
                            <ListItem
                                {...data[index as number]}
                                hasSelect={hasSelect}
                                key={key}
                                style={style}
                                divider={divider}
                                selected={selectedIndex === index}
                                onClick={() => this.onItemClick(index as number, isEmpty(rows) || !rows.disabled)}
                                measure={measure}
                            />
                        ) }
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
                ref={this.setListContainerRef as LegacyRef<HTMLDivElement>}
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
                                        ref={this.setVirtualizerRef as never}
                                        width={width}
                                        height={maxHeight}
                                        deferredMeasurementCache={this.cache}
                                        rowHeight={this.cache.rowHeight}
                                        rowRenderer={this.renderRow as unknown as ListRowRenderer}
                                        rowCount={data.length}
                                        overscanRowCount={5}
                                    />
                                )}
                            </AutoSizer>
                        ) : (
                            <WindowScroller
                                ref={this.setWindowScrollerRef as LegacyRef<WindowScroller>}
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
                                                ref={this.setVirtualizerRef as never}
                                                autoHeight
                                                height={height}
                                                isScrolling={isScrolling}
                                                onScroll={onChildScroll}
                                                overscanRowCount={5}
                                                rowCount={data.length}
                                                rowHeight={this.cache.rowHeight}
                                                rowRenderer={this.renderRow as unknown as ListRowRenderer}
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
}

export { List }

export default List
