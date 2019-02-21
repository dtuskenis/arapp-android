package com.dtuskenis.arapp.functional

typealias Cancel = Function0<Unit>

typealias Run = Function0<Unit>

typealias Accept<T> = Function1<T, Unit>