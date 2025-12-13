package com.example.projek_map.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PengumumanDao_Impl implements PengumumanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pengumuman> __insertionAdapterOfPengumuman;

  private final EntityDeletionOrUpdateAdapter<Pengumuman> __deletionAdapterOfPengumuman;

  private final EntityDeletionOrUpdateAdapter<Pengumuman> __updateAdapterOfPengumuman;

  public PengumumanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPengumuman = new EntityInsertionAdapter<Pengumuman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pengumuman` (`id`,`judul`,`isi`,`tanggal`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pengumuman entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getJudul() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getJudul());
        }
        if (entity.getIsi() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIsi());
        }
        if (entity.getTanggal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTanggal());
        }
      }
    };
    this.__deletionAdapterOfPengumuman = new EntityDeletionOrUpdateAdapter<Pengumuman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pengumuman` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pengumuman entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPengumuman = new EntityDeletionOrUpdateAdapter<Pengumuman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pengumuman` SET `id` = ?,`judul` = ?,`isi` = ?,`tanggal` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pengumuman entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getJudul() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getJudul());
        }
        if (entity.getIsi() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIsi());
        }
        if (entity.getTanggal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTanggal());
        }
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insertPengumuman(final Pengumuman pengumuman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPengumuman.insert(pengumuman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllPengumuman(final List<Pengumuman> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPengumuman.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePengumuman(final Pengumuman pengumuman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPengumuman.handle(pengumuman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePengumuman(final Pengumuman pengumuman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPengumuman.handle(pengumuman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllPengumuman(final Continuation<? super List<Pengumuman>> $completion) {
    final String _sql = "SELECT * FROM pengumuman ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pengumuman>>() {
      @Override
      @NonNull
      public List<Pengumuman> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfJudul = CursorUtil.getColumnIndexOrThrow(_cursor, "judul");
          final int _cursorIndexOfIsi = CursorUtil.getColumnIndexOrThrow(_cursor, "isi");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final List<Pengumuman> _result = new ArrayList<Pengumuman>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pengumuman _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpJudul;
            if (_cursor.isNull(_cursorIndexOfJudul)) {
              _tmpJudul = null;
            } else {
              _tmpJudul = _cursor.getString(_cursorIndexOfJudul);
            }
            final String _tmpIsi;
            if (_cursor.isNull(_cursorIndexOfIsi)) {
              _tmpIsi = null;
            } else {
              _tmpIsi = _cursor.getString(_cursorIndexOfIsi);
            }
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            _item = new Pengumuman(_tmpId,_tmpJudul,_tmpIsi,_tmpTanggal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPengumumanById(final int id,
      final Continuation<? super Pengumuman> $completion) {
    final String _sql = "SELECT * FROM pengumuman WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Pengumuman>() {
      @Override
      @Nullable
      public Pengumuman call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfJudul = CursorUtil.getColumnIndexOrThrow(_cursor, "judul");
          final int _cursorIndexOfIsi = CursorUtil.getColumnIndexOrThrow(_cursor, "isi");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final Pengumuman _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpJudul;
            if (_cursor.isNull(_cursorIndexOfJudul)) {
              _tmpJudul = null;
            } else {
              _tmpJudul = _cursor.getString(_cursorIndexOfJudul);
            }
            final String _tmpIsi;
            if (_cursor.isNull(_cursorIndexOfIsi)) {
              _tmpIsi = null;
            } else {
              _tmpIsi = _cursor.getString(_cursorIndexOfIsi);
            }
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            _result = new Pengumuman(_tmpId,_tmpJudul,_tmpIsi,_tmpTanggal);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMaxId(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(id) FROM pengumuman";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
