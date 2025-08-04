import { defineAsync } from '../../core/factory/defineAsync'

import { HtmlWidget } from './Html/HtmlWidget'
import { FormWidget } from './Form/FormWidget'
import { AdvancedTableWidget } from './AdvancedTable'

export default {
    HtmlWidget,
    FormWidget,
    AdvancedTableWidget,
    ListWidget: defineAsync(() => import('./List/ListWidget')
        .then(({ ListWidget }) => ListWidget)),
    TreeWidget: defineAsync(() => import('./Tree/TreeWidget')
        .then(({ TreeWidget }) => TreeWidget)),
    ChartWidget: defineAsync(() => import('./Chart/ChartWidget')
        .then(({ ChartWidget }) => ChartWidget)),
    CalendarWidget: defineAsync(() => import('./Calendar/CalendarWidget')
        .then(({ CalendarWidget }) => CalendarWidget)),
    TilesWidget: defineAsync(() => import('./Tiles/TilesWidget')
        .then(({ TilesWidget }) => TilesWidget)),
    CardsWidget: defineAsync(() => import('./Cards/CardsWidget')
        .then(({ CardsWidget }) => CardsWidget)),
}
