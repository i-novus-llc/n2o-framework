import React from 'react'
import { connect } from 'react-redux'
import classNames from 'classnames'
import PropTypes from 'prop-types'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'
import { createStructuredSelector } from 'reselect'
import map from 'lodash/map'
import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import find from 'lodash/find'

import widgetContainer from '../WidgetContainer'
import withColumn from '../Table/withColumn'
import TableCell from '../Table/TableCell'
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { withContainerLiveCycle } from '../Table/TableContainer'
import { setTableSelectedId } from '../../../ducks/widgets/store'
import { makeWidgetPageSelector } from '../../../ducks/widgets/selectors'

// eslint-disable-next-line import/no-named-as-default
import List from './List'

const ReduxCell = withColumn(TableCell)

/**
 * Контейнер виджета ListWidget
 * @reactProps {string} widgetId - id виджета
 * @reactProps {object} toolbar - конфиг тулбара
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {object} actions - объект экшенов
 * @reactProps {string} pageId - id страницы
 * @reactProps {object} paging - конфиг пагинации
 * @reactProps {string} className - класс
 * @reactProps {object} style - объект стилей
 * @reactProps {object} filter - конфиг фильтра
 * @reactProps {object} dataProvider - конфиг dataProvider
 * @reactProps {boolean} fetchOnInit - флаг запроса при инициализации
 * @reactProps {object} list - объект конфиг секций в виджете
 * @reactProps {object|null} rowClick - кастомный клик
 * @reactProps {boolean} hasMoreButton - флаг включения загрузки по нажатию на кнопку
 * @reactProps {number} maxHeight - максимальная высота виджета
 * @reactProps {boolean} fetchOnScroll - запрос при скролле
 * @reactProps {boolean} hasSelect - флаг выбора строк
 */
class ListContainer extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            needToCombine: false,
            datasource: props.datasource,
        }

        this.getWidgetProps = this.getWidgetProps.bind(this)
        this.mapSectionComponents = this.mapSectionComponents.bind(this)
        this.renderCell = this.renderCell.bind(this)
        this.handleItemClick = this.handleItemClick.bind(this)
        this.handleFetchMore = this.handleFetchMore.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { datasource: prevDatasource } = prevProps
        const { datasource: currentDatasource, onResolve, selectedId } = this.props
        const { needToCombine } = this.state

        if (currentDatasource && !isEqual(prevDatasource, currentDatasource)) {
            // noinspection JSUnusedAssignment
            let newDatasource = []
            const { datasource } = this.state

            if (needToCombine) {
                newDatasource = [...datasource, ...currentDatasource]
            } else {
                newDatasource = currentDatasource.slice()
            }

            this.setState(
                {
                    needToCombine: false,
                    datasource: newDatasource,
                },
                () => {
                    const model = selectedId
                        ? find(currentDatasource, item => item.id === selectedId)
                        : currentDatasource[0]

                    if (model) { onResolve(model) }
                },
            )
        }
    }

    renderCell(section) {
        const { widgetId } = this.props

        if (!section) { return }

        // eslint-disable-next-line consistent-return
        return (
            <ReduxCell
                {...section}
                widgetId={widgetId}
                positionFixed={false}
                modifiers={{}}
                className={classNames('n2o-widget-list-cell', get(section, 'className', ''))}
            />
        )
    }

    handleItemClick(index) {
        const { onResolve, datasource, rowClick, onRowClickAction } = this.props

        onResolve(datasource[index])
        if (rowClick) {
            onRowClickAction()
        }
    }

    handleFetchMore() {
        const { page, datasource, onFetch } = this.props

        if (!isEmpty(datasource)) {
            this.setState({ needToCombine: true }, () => onFetch({ page: page + 1 }))
        }
    }

    mapSectionComponents() {
        const { list } = this.props
        const { datasource } = this.state

        return map(datasource, (item) => {
            const mappedSection = {}

            forOwn(list, (v, k) => {
                mappedSection[k] = this.renderCell({
                    ...list[k],
                    id: v.id,
                    model: item,
                })
            })

            return mappedSection
        })
    }

    getWidgetProps() {
        const {
            hasMoreButton,
            rowClick,
            placeholder,
            maxHeight,
            fetchOnScroll,
            divider,
            hasSelect,
            selectedId,
            rows,
            t,
        } = this.props

        return {
            onFetchMore: this.handleFetchMore,
            onItemClick: this.handleItemClick,
            data: this.mapSectionComponents(),
            rowClick,
            hasMoreButton,
            placeholder,
            maxHeight,
            fetchOnScroll,
            divider,
            hasSelect,
            selectedId,
            rows,
            t,
        }
    }

    render() {
        return <List {...this.getWidgetProps()} />
    }
}

ListContainer.propTypes = {
    widgetId: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    actions: PropTypes.object,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    list: PropTypes.object,
    fetchOnScroll: PropTypes.bool,
    rowClick: PropTypes.func,
    hasMoreButton: PropTypes.bool,
    maxHeight: PropTypes.number,
    datasource: PropTypes.array,
    hasSelect: PropTypes.bool,
    onResolve: PropTypes.func,
    selectedId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onRowClickAction: PropTypes.func,
    page: PropTypes.func,
    onFetch: PropTypes.func,
    placeholder: PropTypes.string,
    divider: PropTypes.bool,
    rows: PropTypes.object,
    t: PropTypes.func,
}

ListContainer.defaultProps = {
    datasource: [],
    rowClick: null,
    hasMoreButton: false,
    toolbar: {},
    disabled: false,
    actions: {},
    className: '',
    style: {},
    filter: {},
    list: {},
    fetchOnScroll: false,
    hasSelect: false,
}

const mapStateToProps = createStructuredSelector({
    page: (state, props) => makeWidgetPageSelector(props.widgetId)(state),
})

export default compose(
    withTranslation(),
    widgetContainer(
        {
            mapProps: props => ({
                ...props,
                onResolve: (newModel) => {
                    props.onResolve(newModel)
                    // eslint-disable-next-line eqeqeq
                    if (props.selectedId != newModel.id) {
                        props.dispatch(setTableSelectedId(props.widgetId, newModel.id))
                    }
                },
            }),
        },
        'ListWidget',
    ),
    withContainerLiveCycle,
    withWidgetHandlers,
    connect(mapStateToProps),
)(ListContainer)
