import PropTypes from 'prop-types'

export const defaultChartProps = {
    width: 200,
    height: 200,
}

export const chartTypes = {
    layout: PropTypes.string,
    width: PropTypes.number,
    height: PropTypes.number,
    margin: PropTypes.object,
    data: PropTypes.array,
}

export const pieTypes = PropTypes.shape({
    cx: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    cy: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    innerRadius: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    outerRadius: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    startAngle: PropTypes.number,
    endAngle: PropTypes.number,
    minAngle: PropTypes.number,
    paddingAngle: PropTypes.number,
    nameKey: PropTypes.string,
    dataKey: PropTypes.string,
    legendType: PropTypes.oneOf([
        'line',
        'square',
        'rect',
        'circle',
        'cross',
        'diamond',
        'star',
        'triangle',
        'wye',
        'none',
    ]),
    label: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.object,
        PropTypes.node,
        PropTypes.func,
    ]),
    labelLine: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.object,
        PropTypes.node,
        PropTypes.func,
    ]),
    fill: PropTypes.string,
    animationBegin: PropTypes.number,
    animationEasing: PropTypes.oneOf([
        'ease',
        'ease-in',
        'ease-out',
        'ease-in-out',
        'linear',
    ]),
})

export const XAxisTypes = PropTypes.shape({
    hide: PropTypes.bool,
    dataKey: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    width: PropTypes.number,
    height: PropTypes.number,
    orientation: PropTypes.oneOf(['bottom', 'top']),
    type: PropTypes.oneOf(['number', 'category']),
    allowDecimals: PropTypes.bool,
    allowDataOverflow: PropTypes.bool,
    allowDuplicatedCategory: PropTypes.bool,
    tickCount: PropTypes.number,
    interval: PropTypes.oneOfType([
        PropTypes.number,
        PropTypes.oneOf(['preserveStart', 'preserveEnd', 'preserveStartEnd']),
    ]),
    padding: PropTypes.object,
    minTickGap: PropTypes.number,
    axisLine: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
    tickLine: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
    tickSize: PropTypes.number,
    label: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.node,
        PropTypes.object,
    ]),
})
export const YAxisTypes = PropTypes.shape({
    hide: PropTypes.bool,
    dataKey: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    width: PropTypes.number,
    height: PropTypes.number,
    orientation: PropTypes.oneOf(['bottom', 'top']),
    type: PropTypes.oneOf(['number', 'category']),
    allowDecimals: PropTypes.bool,
    allowDataOverflow: PropTypes.bool,
    allowDuplicatedCategory: PropTypes.bool,
    tickCount: PropTypes.number,
    interval: PropTypes.oneOfType([
        PropTypes.number,
        PropTypes.oneOf(['preserveStart', 'preserveEnd', 'preserveStartEnd']),
    ]),
    padding: PropTypes.object,
    minTickGap: PropTypes.number,
    axisLine: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
    tickLine: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
    tickSize: PropTypes.number,
    label: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.node,
        PropTypes.object,
    ]),
})

export const cartesianGridTypes = PropTypes.shape({
    x: PropTypes.number,
    y: PropTypes.number,
    width: PropTypes.number,
    height: PropTypes.number,
    horizontal: PropTypes.bool,
    vertical: PropTypes.bool,
    horizontalPoints: PropTypes.array,
    verticalPoints: PropTypes.array,
    strokeDasharray: PropTypes.string,
})

export const tooltipTypes = PropTypes.shape({
    separator: PropTypes.string,
    offset: PropTypes.number,
    filterNull: PropTypes.bool,
    itemStyle: PropTypes.object,
    wrapperStyle: PropTypes.object,
    contentStyle: PropTypes.object,
    labelStyle: PropTypes.object,
    viewBox: PropTypes.shape({
        x: PropTypes.number,
        y: PropTypes.number,
        width: PropTypes.number,
        height: PropTypes.number,
    }),
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
})

export const legendTypes = PropTypes.shape({
    width: PropTypes.number,
    height: PropTypes.number,
    layout: PropTypes.oneOf(['horizontal', 'vertical']),
    align: PropTypes.oneOf(['left', 'center', 'right']),
    verticalAlign: PropTypes.oneOf(['top', 'middle', 'bottom']),
    iconSize: PropTypes.number,
    margin: PropTypes.object,
    wrapperStyle: PropTypes.number,
})

export const linesTypes = PropTypes.arrayOf(
    PropTypes.shape({
        type: PropTypes.oneOf([
            'basis',
            'basisClosed',
            'basisOpen',
            'linear',
            'linearClosed',
            'natural',
            'monotoneX',
            'monotoneY',
            'mpnotone',
            'step',
            'stepBefore',
            'stepAfter',
        ]),
        dataKey: PropTypes.oneOfType([
            PropTypes.number,
            PropTypes.string,
            PropTypes.func,
        ]),
        stroke: PropTypes.string,
        legendType: PropTypes.oneOf([
            'line',
            'square',
            'rect',
            'circle',
            'cross',
            'diamond',
            'star',
            'triangle',
            'wye',
            'none',
        ]),
        dot: PropTypes.oneOfType([
            PropTypes.bool,
            PropTypes.object,
            PropTypes.node,
            PropTypes.func,
        ]),
        activeDot: PropTypes.oneOfType([
            PropTypes.bool,
            PropTypes.object,
            PropTypes.node,
            PropTypes.func,
        ]),
        label: PropTypes.oneOfType([
            PropTypes.bool,
            PropTypes.object,
            PropTypes.node,
            PropTypes.func,
        ]),
        layout: PropTypes.oneOf(['horizontal', 'vertical']),
    }),
)

export const areasTypes = PropTypes.arrayOf(
    PropTypes.shape({
        ...linesTypes,
        fill: PropTypes.string,
        stackId: PropTypes.number,
    }),
)

export const barChartTypes = {
    ...chartTypes,
    stackOffset: PropTypes.oneOf([
        'expand',
        'none',
        'wiggle',
        'silhouette',
        'sign',
    ]),
    barCategoryGap: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    barGap: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    barSize: PropTypes.number,
    maxBarSize: PropTypes.number,
    reverseStackOrder: PropTypes.bool,
}

export const barsTypes = PropTypes.arrayOf(
    PropTypes.shape({
        stackId: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
        fill: PropTypes.string,
        layout: PropTypes.oneOf(['horizontal', 'vertical']),
        dataKey: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
        legendType: PropTypes.oneOf([
            'line',
            'square',
            'rect',
            'circle',
            'cross',
            'diamond',
            'star',
            'triangle',
            'wye',
            'none',
        ]),
        label: PropTypes.oneOfType([
            PropTypes.bool,
            PropTypes.object,
            PropTypes.node,
            PropTypes.func,
        ]),
        barSize: PropTypes.number,
        maxBarSize: PropTypes.number,
        background: PropTypes.oneOfType([
            PropTypes.bool,
            PropTypes.object,
            PropTypes.node,
            PropTypes.func,
        ]),
    }),
)
