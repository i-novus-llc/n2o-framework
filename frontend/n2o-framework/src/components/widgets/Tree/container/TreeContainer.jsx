import { compose, withHandlers, withProps } from 'recompose'
import map from 'lodash/map'
import filter from 'lodash/filter'
import some from 'lodash/some'
import isArray from 'lodash/isArray'
import toString from 'lodash/toString'

import Tree from '../component/Tree'

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

const TreeContainer = compose(
    withProps(({ models, ...rest }) => {
        const { datasource, resolve } = models

        return {
            datasource: mapToString(datasource, rest),
            resolveModel: mapToString(resolve || {}, rest),
            selectedId: resolve?.id,
        }
    }),
    withWidgetHandlers,
)(Tree)

TreeContainer.propTypes = propTypes
TreeContainer.defaultProps = defaultProps

export default TreeContainer
