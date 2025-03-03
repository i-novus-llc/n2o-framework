import React from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle, withHandlers } from 'recompose'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { Sorter } from '../../../snippets/Sorter/Sorter'
import { changeFrozenColumn, changeColumnVisibility } from '../../../../ducks/columns/store'
import { useDataSourceMethodsContext } from '../../../../core/widget/context'

/**
 * Текстовый заголовок таблицы с возможностью сортировки
 * @reactProps {string} [sortingParam] - параметр сортировки
 * @reactProps {string} soring - текущее направление сортировки
 * @reactProps {string} label - Текст заголовка столбца
 * @reactProps {function} onSort - эвент сортировки. Вызывает при смене направления сортировки
 */
const TextTableHeader = ({ sortingParam, sorting, label, style }) => {
    const { setSorting } = useDataSourceMethodsContext()

    return (
        <span className="n2o-advanced-table-header-title" style={style}>
            <Sorter visible={Boolean(sortingParam)} sorting={sorting} sortingParam={sortingParam} onSort={setSorting}>
                <Text>{label}</Text>
            </Sorter>
            {!sortingParam && <Text>{label}</Text>}
        </span>
    )
}

TextTableHeader.propTypes = {
    sortingParam: PropTypes.string,
    sorting: PropTypes.string,
    label: PropTypes.string,
    style: PropTypes.object,
}

const enhance = compose(
    withHandlers({
        toggleVisibility: ({ dispatch, widgetId, columnId }) => (visible) => {
            if (!dispatch) {
                return
            }

            dispatch(changeColumnVisibility(widgetId, columnId, visible))
            dispatch(changeFrozenColumn(widgetId, columnId))
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
