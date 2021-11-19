import { lifecycle } from 'recompose'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
    // eslint-disable-next-line eqeqeq
    const predicate = ({ id }) => id == selectedId

    return isEqual(find(data1, predicate), find(data2, predicate))
}

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
