import React from "react";
import TabsWrapper from '@theme/Tabs';
import TabItem from '@theme/TabItem';



export function Tabs({ values, className, Component }) {
    return (
            <TabsWrapper
                    className={className}
                    defaultValue="first"
                    values={values}>
                {values.map(({ value }) =>
                        <TabItem key={value} value={value}>
                            <Component value={value} />
                        </TabItem>)
                }
            </TabsWrapper>
    )
}
