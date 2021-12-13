import React from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle, withHandlers } from 'recompose'
import { batchActions } from 'redux-batched-actions'

// eslint-disable-next-line import/no-named-as-default
import Sorter from '../../../snippets/Sorter/Sorter'
import {
    changeFrozenColumn,
    changeColumnVisibility,
} from '../../../../ducks/columns/store'

/**
 * Текстовый заголовок таблицы с возможностью сортировки
 * @reactProps {string} id - id столбца
 * @reactProps {boolean} sortable - можно ли сортировать столбец
 * @reactProps {string} soring
 * @reactProps {string} label - Текст заголовка столбца
 * @reactProps {function} onSort - эвент сортировки. Вызывает при смене направления сортировки
 */
class TextTableHeader extends React.PureComponent {
    render() {
        const { id, sortable, sorting, label, setSorting, style } = this.props

        return (
            <span className="n2o-advanced-table-header-title" style={style}>
                {sortable ? (
                    <Sorter sorting={sorting} columnKey={id} onSort={setSorting}>
                        {label}
                    </Sorter>
                ) : (
                    label
                )}
            </span>
        )
    }
}

TextTableHeader.propTypes = {
    id: PropTypes.string,
    sortable: PropTypes.bool,
    sorting: PropTypes.string,
    label: PropTypes.string,
    setSorting: PropTypes.func,
    style: PropTypes.object,
}

const enhance = compose(
    withHandlers({
        toggleVisibility: ({ dispatch, widgetId, columnId }) => (visible) => {
            dispatch(
                batchActions([
                    changeColumnVisibility(widgetId, columnId, visible),
                    changeFrozenColumn(widgetId, columnId),
                ]),
            )
        },
    }),
    lifecycle({
        componentDidMount() {
            const { visible, toggleVisibility } = this.props

            if (visible === false) {
                toggleVisibility(visible)
            }
        },
    }),
)

export { TextTableHeader }
export default enhance(TextTableHeader)
