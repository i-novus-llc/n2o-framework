// import { connect } from 'react-redux'
import { lifecycle, compose, withHandlers } from 'recompose'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'
import debounce from 'lodash/debounce'

import widgetContainer from '../WidgetContainer'
import { setTableSelectedId } from '../../../ducks/widgets/store'
import { TABLE } from '../widgetTypes'

// eslint-disable-next-line import/no-named-as-default
import Table from './Table'

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
    // eslint-disable-next-line eqeqeq
    const predicate = ({ id }) => id == selectedId

    return isEqual(find(data1, predicate), find(data2, predicate))
}

export const withWidgetContainer = widgetContainer(
    {
        mapProps: props => ({
            widgetId: props.widgetId,
            pageId: props.pageId,
            headers: props.headers,
            cells: props.cells,
            isAnyTableFocused: props.isAnyTableFocused,
            isActive: props.isActive,
            hasFocus: props.hasFocus,
            hasSelect: props.hasSelect,
            autoFocus: props.autoFocus,
            datasource: props.datasource,
            selectedId: props.selectedId,
            sorting: props.sorting,
            rowColor: props.rowColor,
            onSort: props.onSort,
            onResolve: debounce((newModel) => {
                props.onResolve(newModel)
                // eslint-disable-next-line eqeqeq
                if (props.selectedId != newModel.id) {
                    props.dispatch(setTableSelectedId(props.widgetId, newModel.id))
                }
            }, 100),
            onFocus: props.onFocus,
            size: props.size,
            actions: props.actions,
            redux: true,
            onActionImpl: props.onActionImpl,
            rowClick: props.rowClick,
        }),
    },
    TABLE,
)

export const withContainerLiveCycle = lifecycle({
    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps) {
        const {
            selectedId: prevSelectedId,
            datasource: prevDatasource,
            onResolve,
        } = this.props
        const { hasSelect, datasource, selectedId } = nextProps

        if (
            hasSelect &&
      !isEmpty(datasource) &&
      !isEqual(prevDatasource, datasource) &&
      (!selectedId ||
        !isEqual(prevSelectedId, selectedId) ||
        !isEqualCollectionItemsById(prevDatasource, datasource, selectedId))
        ) {
            // eslint-disable-next-line eqeqeq
            const selectedModel = find(datasource, model => model.id == selectedId)
            const resolveModel = selectedModel || datasource[0]

            onResolve(resolveModel)
        }
    },
})

export const withWidgetHandlers = withHandlers({
    onRowClickAction: ({ rowClick, onActionImpl }) => () => {
        onActionImpl(rowClick)
    },
})

export default compose(
    withWidgetContainer,
    withContainerLiveCycle,
    withWidgetHandlers,
)(Table)
