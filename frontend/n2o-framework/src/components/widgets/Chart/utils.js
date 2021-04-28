import filter from 'lodash/filter'
import merge from 'lodash/merge'
import get from 'lodash/get'
import omit from 'lodash/omit'

export const COLORS = [
    '#9E2B0E',
    '#FF6E4A',
    '#A82255',
    '#FF66A1',
    '#5C255C',
    '#C274C2',
    '#5642A6',
    '#AD99FF',
    '#1F4B99',
    '#669EFF',
    '#008075',
    '#2EE6D6',
    '#1D7324',
    '#62D96B',
    '#728C23',
    '#D1F26D',
    '#A67908',
    '#FFC940',
    '#63411E',
    '#C99765',
]

export const parseData = (data) => {
    if (data.length) {
        return data.map((item) => {
            const { hasLabel, label } = item

            return {
                ...omit(item, ['hasLabel', 'label']),
                label: hasLabel,
                name: label,
            }
        })
    }
}

export const setLineColors = (lines) => {
    if (lines.length) {
        let linesWithColor = []
        const linesWithoutColor = filter(lines, ({ stroke }) => !stroke)

        if (linesWithoutColor.length) {
            linesWithColor = linesWithoutColor.map((item, i) => ({
                ...item,
                stroke: COLORS[i % COLORS.length],
            }))
        }

        return merge(lines, linesWithColor)
    }
}

export const createDomain = (yaxis) => {
    const yMin = get(yaxis, 'min')
    const yMax = get(yaxis, 'max')

    return [yMin || 'auto', yMax || 'dataMax']
}
