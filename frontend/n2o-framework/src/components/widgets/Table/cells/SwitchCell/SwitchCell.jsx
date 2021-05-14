import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'

// eslint-disable-next-line import/no-named-as-default
import Factory from '../../../../../core/factory/Factory'
import { CELLS } from '../../../../../core/factory/factoryLevels'

function SwitchCell({
    model,
    switchFieldId,
    switchList,
    switchDefault,
    ...props
}) {
    const currentCellType = model[switchFieldId]
    const cellProps = get(switchList, currentCellType, switchDefault)

    return <Factory level={CELLS} model={model} {...props} {...cellProps} />
}

SwitchCell.propTypes = {
    model: PropTypes.object,
    switchFieldId: PropTypes.string,
    switchList: PropTypes.object,
    switchDefault: PropTypes.object,
    /**
   * props: метаданные,
   * из которых по switchFieldId
   * в switchList берется Cell.
   * Если ключ не подходит,
   * Cell по switchDefault
   * */
    props: PropTypes.object,
}

export default SwitchCell
