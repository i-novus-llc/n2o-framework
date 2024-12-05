type ResolutionType = 'mobile' | 'tablet' | 'desktop'

interface Mods {
    isMobile: boolean;
    isTablet: boolean;
    isDesktop: boolean;
}

function getResolution(width: number): Mods {
    const mods: Mods = {
        isMobile: false,
        isTablet: false,
        isDesktop: false,
    }

    const resolution = {
        isMobile: width < 767,
        isTablet: width < 1367,
        isDesktop: width > 1368,
    }

    if (resolution.isMobile) {
        mods.isMobile = true
    } else if (resolution.isTablet) {
        mods.isTablet = true
    } else {
        mods.isDesktop = true
    }

    return mods
}

export function calcCols(sm: number, md: number, lg: number, width: number): number {
    const { isMobile, isTablet } = getResolution(width)

    let col: number

    if (isMobile) {
        col = sm
    } else if (isTablet) {
        col = md
    } else {
        col = lg
    }

    return Math.round(12 / col)
}
