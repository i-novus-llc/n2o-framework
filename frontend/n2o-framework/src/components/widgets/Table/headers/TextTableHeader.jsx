import React from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle, withHandlers } from 'recompose'
import { batchActions } from 'redux-batched-actions'

import Sorter from '../../../snippets/Sorter/Sorter'
import {
    changeFrozenColumn,
    changeColumnVisiblity,
} from '../../../../actions/columns'

/**
 * Текстовый заголовок таблицы с возможностью сортировки
 * @reactProps {string} id - id столбца
 * @reactProps {boolean} sortable - можно ли сортировать столбец
 * @reactProps {string} soring
 * @reactProps {string} label - Текст заголовка столбца
 * @reactProps {function} onSort - эвент сортировки. Вызывает при смене направления сортировки
 */
class TextTableHeader extends React.Component {
    render() {
        const { id, sortable, sorting, label, onSort } = this.props

        return (
            <span>
                {sortable ? (
                    <Sorter sorting={sorting} columnKey={id} onSort={onSort}>
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
    onSort: PropTypes.func,
}

const enhance = compose(
    withHandlers({
        toggleVisibility: ({ dispatch, widgetId, columnId }) => (visible) => {
            dispatch(
                batchActions([
                    changeColumnVisiblity(widgetId, columnId, visible),
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
