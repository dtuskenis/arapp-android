package com.dtuskenis.arapp.extensions

fun <T1, T2> ClosedRange<T1>.map(transform: (T1) -> T2): ClosedRange<T2> where T1: Comparable<T1>, T2: Comparable<T2> {
    val originalRange = this

    return object : ClosedRange<T2> {

        override val endInclusive: T2 get() = transform(originalRange.endInclusive)

        override val start: T2 get() = transform(originalRange.start)
    }
}