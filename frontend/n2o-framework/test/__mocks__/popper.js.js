import StockPopperJs from 'popper.js'

export default class PopperJs {
    static placements = StockPopperJs.placements;

    constructor() {
        return {
            destroy: () => {},
            scheduleUpdate: () => {},
            update: () => {},
        }
    }
}
