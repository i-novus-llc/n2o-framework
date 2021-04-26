import filter from 'lodash/filter'
import merge from 'lodash/merge'

const COLORS = [
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
