import { compose, withHandlers } from 'recompose'
import map from 'lodash/map'
import filter from 'lodash/filter'
import some from 'lodash/some'
import isArray from 'lodash/isArray'
import toString from 'lodash/toString'
import { connect } from 'react-redux'

import Tree from '../component/Tree'
import { ModelPrefix } from '../../../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../../../ducks/models/selectors'

import { propTypes, defaultProps } from './allProps'

const toStringData = ({ valueFieldId, parentFieldId }) => dt => ({
    ...dt,
    [valueFieldId]: dt[valueFieldId] && toString(dt[valueFieldId]),
    [parentFieldId]: dt[parentFieldId] && toString(dt[parentFieldId]),
})

const mapToString = (data, params) => (isArray(data) ? map(data, toStringData(params)) : toStringData(params)(data))

export const withWidgetHandlers = withHandlers({
    onRowClickAction: ({ rowClick, dispatch }) => () => {
        dispatch(rowClick.action)
    },

    onResolve: props => (keys) => {
        const {
            setResolve,
            datasource,
            valueFieldId,
            multiselect,
            rowClick,
            dispatch,
        } = props

        // eslint-disable-next-line eqeqeq
        const value = filter(datasource, data => some(keys, key => key == data[valueFieldId]))

        if (multiselect) {
            setResolve(value)
        } else {
            setResolve(value ? value[0] : null)
        }

        if (rowClick) { dispatch(rowClick.action) }
    },
})

const mapStateToProps = (state, props) => {
    const datasourceModel = getModelByPrefixAndNameSelector(ModelPrefix.source, props.datasource, [])(state)
    const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, props.datasource, {})(state)

    return ({
        datasource: mapToString(datasourceModel, props),
        resolveModel: mapToString(resolveModel, props),
        selectedId: resolveModel.id,
    })
}

const TreeContainer = compose(
    connect(mapStateToProps),
    withWidgetHandlers,
)(Tree)

TreeContainer.propTypes = propTypes
TreeContainer.defaultProps = defaultProps

export default TreeContainer
