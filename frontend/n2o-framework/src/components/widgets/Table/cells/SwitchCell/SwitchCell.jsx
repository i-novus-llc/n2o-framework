import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'

import { Factory } from '../../../../../core/factory/Factory'
import { CELLS } from '../../../../../core/factory/factoryLevels'

function SwitchCell({
    model,
    switchFieldId,
    switchList,
    switchDefault,
    style: propsStyle,
    ...props
}) {
    const currentCellType = get(model, switchFieldId)
    const cellProps = get(switchList, currentCellType, switchDefault)
    const currentCellStyle = get(cellProps, 'elementAttributes.style', {})

    const style = { ...propsStyle, ...currentCellStyle }

    return <Factory level={CELLS} model={model} {...props} {...cellProps} style={style} />
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
