import { compose, withHandlers, withProps } from 'recompose'
import map from 'lodash/map'
import filter from 'lodash/filter'
import some from 'lodash/some'
import isArray from 'lodash/isArray'
import toString from 'lodash/toString'

import Tree from '../component/Tree'
import widgetContainer from '../../WidgetContainer'
import { setTableSelectedId } from '../../../../ducks/widgets/store'
import { TREE } from '../../widgetTypes'

import { propTypes, defaultProps } from './allProps'

export const withWidgetContainer = widgetContainer(
    {
        mapProps: props => ({
            widgetId: props.widgetId,
            pageId: props.pageId,
            isActive: props.isActive,
            hasFocus: props.hasFocus,
            hasSelect: props.hasSelect,
            autoFocus: props.autoFocus,
            datasource: props.datasource,
            resolveModel: props.resolveModel,
            onResolve: (newModel) => {
                props.onResolve(newModel)
                // eslint-disable-next-line eqeqeq
                if (props.selectedId != newModel.id) {
                    props.dispatch(setTableSelectedId(props.widgetId, newModel.id))
                }
            },
            onFocus: props.onFocus,
            size: props.size,
            redux: true,
            dispatch: props.dispatch,
            rowClick: props.rowClick,

            childIcon: props.childIcon,
            multiselect: props.multiselect,
            showLine: props.showLine,
            filter: props.filter,
            expandBtn: props.expandBtn,
            bulkData: props.bulkData,
            parentFieldId: props.parentFieldId,
            valueFieldId: props.valueFieldId,
            labelFieldId: props.labelFieldId,
            iconFieldId: props.iconFieldId,
            imageFieldId: props.imageFieldId,
            badgeFieldId: props.badgeFieldId,
            badgeColorFieldId: props.badgeColorFieldId,
            hasCheckboxes: props.hasCheckboxes,
            draggable: props.draggable,
            childrenFieldId: props.childrenFieldId,
        }),
    },
    TREE,
)

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
            onResolve,
            datasource,
            valueFieldId,
            multiselect,
            rowClick,
            dispatch,
        } = props

        // eslint-disable-next-line eqeqeq
        const value = filter(datasource, data => some(keys, key => key == data[valueFieldId]))

        if (multiselect) {
            onResolve(value)
        } else {
            onResolve(value ? value[0] : null)
        }

        if (rowClick) { dispatch(rowClick.action) }
    },
})

const TreeContainer = compose(
    withWidgetContainer,
    withWidgetHandlers,
    withProps(({ datasource, resolveModel, ...rest }) => ({
        datasource: mapToString(datasource || [], rest),
        resolveModel: mapToString(resolveModel, rest),
    })),
)(Tree)

TreeContainer.propTypes = propTypes
TreeContainer.defaultProps = defaultProps

export default TreeContainer
