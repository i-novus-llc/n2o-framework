import React from 'react'
import { withTranslation } from 'react-i18next'
import map from 'lodash/map'
import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import find from 'lodash/find'
import flowRight from 'lodash/flowRight'
import { connect } from 'react-redux'

import withColumn from '../Table/withColumn'
import { TableCell } from '../Table/TableCell'
import { withWidgetHandlers } from '../hocs/withWidgetHandlers'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { State as GlobalState } from '../../../ducks/State'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

import { List } from './List'
import { type ListContainerProps, type ListDataItem } from './types'

const ReduxCell = withColumn(TableCell)

export interface State {
    needToCombine: boolean
    datasource: ListContainerProps['datasourceModel']
}

/**
 * Контейнер виджета ListWidget
 * @reactProps {object} list - объект конфиг секций в виджете
 * @reactProps {object|null} rowClick - кастомный клик
 * @reactProps {boolean} hasMoreButton - флаг включения загрузки по нажатию на кнопку
 * @reactProps {number} maxHeight - максимальная высота виджета
 * @reactProps {boolean} fetchOnScroll - запрос при скролле
 * @reactProps {boolean} hasSelect - флаг выбора строк
 */
class ListContainer extends React.Component<ListContainerProps, State> {
    constructor(props: ListContainerProps) {
        super(props)

        const { datasourceModel } = props

        this.state = {
            needToCombine: false,
            datasource: datasourceModel,
        }
    }

    componentDidUpdate({ datasourceModel: prevDatasource }: ListContainerProps) {
        const { setResolve, datasourceModel: currentDatasource, selectedId } = this.props
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

                    if (model) { setResolve(model) }
                },
            )
        }
    }

    renderCell = (section: { columnId: string; id: string; model: Record<string, unknown> }) => {
        const { id, datasource } = this.props

        if (!section) { return }

        // eslint-disable-next-line consistent-return
        return (
            <ReduxCell
                {...section}
                widgetId={id}
                datasource={datasource}
                positionFixed={false}
                modifiers={{}}
            />
        )
    }

    handleItemClick = (index: number) => {
        const { setResolve, datasourceModel, rowClick, onRowClickAction } = this.props
        const model = datasourceModel[index]

        setResolve(model)
        if (rowClick) {
            onRowClickAction(model)
        }
    }

    handleFetchMore = () => {
        const { page, datasourceModel, setPage } = this.props

        if (!isEmpty(datasourceModel)) {
            this.setState({ needToCombine: true }, () => setPage(page + 1))
        }
    }

    mapSectionComponents = () => {
        const { list } = this.props
        const { datasource } = this.state

        return map(datasource, (item) => {
            const mappedSection = {} as ListDataItem

            forOwn(list, (v, k) => {
                // @ts-ignore FIXME виджет требует рефакторинга
                mappedSection[k] = this.renderCell({ ...list[k], id: v.id, model: item })
            })

            return mappedSection
        })
    }

    getWidgetProps = () => {
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

    static defaultProps = {
        rowClick: null,
        hasMoreButton: false,
        toolbar: EMPTY_OBJECT,
        disabled: false,
        className: '',
        style: EMPTY_OBJECT,
        filter: EMPTY_OBJECT,
        list: EMPTY_OBJECT,
        fetchOnScroll: false,
        hasSelect: false,
    }
}

const mapStateToProps = (state: GlobalState, { datasource }: ListContainerProps) => ({
    selectedId: (dataSourceModelByPrefixSelector(datasource, ModelPrefix.active)(state) as { id: string })?.id,
    datasourceModel: dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state),
})

export default flowRight(
    withTranslation(),
    withWidgetHandlers,
    connect(mapStateToProps),
)(ListContainer)
